package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GenericSupplierDto;
import dev.eshan.productservice.events.EventName;
import dev.eshan.productservice.events.ProductEvent;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.models.Category;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.models.Supplier;
import dev.eshan.productservice.repositories.CategoryRepository;
import dev.eshan.productservice.repositories.ProductRepository;
import dev.eshan.productservice.repositories.SupplierRepository;
import dev.eshan.productservice.services.interfaces.ProductService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final RedisTemplate<String, Object> redisTemplate;

    ProductServiceImpl(ProductRepository productRepository,
                       CategoryRepository categoryRepository,
                       SupplierRepository supplierRepository, ApplicationEventPublisher eventPublisher,
                       RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.eventPublisher = eventPublisher;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<GenericProductDto> getProducts() {
        try {
            List<GenericProductDto> productDtoList = getProductListFromRedis();
            if (productDtoList != null && !productDtoList.isEmpty()) {
                return productDtoList;
            }
            Optional<List<Product>> products = Optional.of(productRepository.findAll());
            List<GenericProductDto> genericProductDtos = new ArrayList<>();
            for (Product product : products.get()) {
                GenericProductDto genericProductDto = new GenericProductDto();
                genericProductDto.setId(product.getId());
                genericProductDto.setTitle(product.getTitle());
                genericProductDto.setDescription(product.getDescription());
                genericProductDto.setSpecifications(product.getSpecifications());
                genericProductDto.setImageUrl(product.getImageUrl());
                genericProductDto.setPrice(product.getPrice());
                genericProductDto.setStockLevel(product.getStockLevel());
                genericProductDto.setLowStockThreshold(product.getLowStockThreshold());
                genericProductDtos.add(genericProductDto);
                genericProductDto.setCategory(GenericCategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build());
            }
            saveProductListToRedis(genericProductDtos);
            return genericProductDtos;
        } catch (Exception e) {
            log.error("Error occurred while fetching products", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching products");
        }
    }

    public void saveProductListToRedis(List<GenericProductDto> productList) {
        String cacheKey = "PRODUCT_LIST";
        redisTemplate.opsForValue().set(cacheKey, productList);
        redisTemplate.expire(cacheKey, 10, TimeUnit.MINUTES);
    }

    public List<GenericProductDto> getProductListFromRedis() {
        return (List<GenericProductDto>) redisTemplate.opsForValue().get("PRODUCT_LIST");
    }

    @Override
    public GenericProductDto getProductById(String id, Long userIdTryingToAccess) throws NotFoundException {
        GenericProductDto genericProductDto = (GenericProductDto) redisTemplate.opsForHash().get("PRODUCT", id);
        if (genericProductDto != null && genericProductDto.getStockLevel() > genericProductDto.getLowStockThreshold()) {
            return genericProductDto;
        }
        GenericProductDto genericProductDtoFromDB = getProductFromStoreById(id);
        redisTemplate.opsForHash().put("PRODUCT", id, genericProductDtoFromDB);
        redisTemplate.expire("PRODUCT", 10, TimeUnit.MINUTES);
        return genericProductDtoFromDB;
    }

    @Transactional
    public GenericProductDto getProductFromStoreById(String id) throws NotFoundException {
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            Product product = productOptional.orElseThrow(() -> new NotFoundException("Product not found by id: " + id));

            GenericProductDto genericProductDto = new GenericProductDto();
            genericProductDto.setId(product.getId());
            genericProductDto.setTitle(product.getTitle());
            genericProductDto.setDescription(product.getDescription());
            genericProductDto.setSpecifications(product.getSpecifications());
            genericProductDto.setImageUrl(product.getImageUrl());
            genericProductDto.setPrice(product.getPrice());
            genericProductDto.setStockLevel(product.getStockLevel());
            genericProductDto.setLowStockThreshold(product.getLowStockThreshold());

            Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
            if (category.isPresent()) {
                // don't set products here to avoid infinite recursion
                // Since the List of products are fetched lazily by default in JPA repositories
                genericProductDto.setCategory(GenericCategoryDto.builder()
                        .id(category.get().getId())
                        .name(category.get().getName())
                        .build());
            }
            return genericProductDto;
        } catch (NotFoundException e) {
            log.error("Product not found by id: " + id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while fetching product by id: " + id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching product by id: " + id);
        }
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto genericProductDto) throws NotFoundException {
        try {
            if (genericProductDto == null) {
                throw new NotFoundException("Product details not provided");
            }
            if (genericProductDto.getCategory() == null || genericProductDto.getCategory().getId() == null) {
                throw new NotFoundException("Category not provided");
            }
            if (genericProductDto.getSupplier() == null || genericProductDto.getSupplier().getId() == null) {
                throw new NotFoundException("Supplier not provided");
            }
            Product product = new Product();
            product.setTitle(genericProductDto.getTitle());
            product.setDescription(genericProductDto.getDescription());
            product.setSpecifications(genericProductDto.getSpecifications());
            product.setImageUrl(genericProductDto.getImageUrl());
            product.setPrice(genericProductDto.getPrice());
            product.setStockLevel(genericProductDto.getStockLevel());
            product.setLowStockThreshold(genericProductDto.getLowStockThreshold());

            Category category = categoryRepository.findById(genericProductDto.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + genericProductDto.getCategory().getId()));
            product.setCategory(category);

            Supplier supplier = supplierRepository.findById(genericProductDto.getSupplier().getId())
                    .orElseThrow(() -> new NotFoundException("Supplier not found by id: " + genericProductDto.getSupplier().getId()));
            product.setSupplier(supplier);

            Product savedProduct = productRepository.save(product);
            genericProductDto.setId(savedProduct.getId());
            genericProductDto.getCategory().setId(savedProduct.getCategory().getId());
            genericProductDto.getSupplier().setId(savedProduct.getSupplier().getId());

            eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_CREATED));
            // Invalidate cache
            redisTemplate.delete("PRODUCT_LIST");
            return genericProductDto;
        } catch (NotFoundException e) {
            log.error("Error occurred while creating product", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while creating product", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while creating product");
        }
    }

    private ProductEvent getProductEvent(GenericProductDto genericProductDto, EventName eventName) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventName(eventName);
        productEvent.setProduct(genericProductDto);
        return productEvent;
    }

    @Override
    public GenericProductDto updateProduct(String id, GenericProductDto genericProductDto) throws NotFoundException {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(id);
            if (existingProductOptional.isEmpty()) {
                throw new NotFoundException("Product not found by id: " + id);
            }

            Product existingProduct = existingProductOptional.get();
            if (genericProductDto.getTitle() != null) {
                existingProduct.setTitle(genericProductDto.getTitle());
            }
            if (genericProductDto.getDescription() != null) {
                existingProduct.setDescription(genericProductDto.getDescription());
            }
            if (genericProductDto.getSpecifications() != null) {
                existingProduct.setSpecifications(genericProductDto.getSpecifications());
            }
            if (genericProductDto.getImageUrl() != null) {
                existingProduct.setImageUrl(genericProductDto.getImageUrl());
            }
            if (genericProductDto.getPrice() != null) {
                existingProduct.setPrice(genericProductDto.getPrice());
            }

            if (genericProductDto.getCategory() != null && genericProductDto.getCategory().getId() != null) {
                Optional<Category> categoryOptional = categoryRepository.findById(genericProductDto.getCategory().getId());
                categoryOptional.ifPresent(existingProduct::setCategory);
                categoryOptional.orElseThrow(() -> new NotFoundException("Category not found by id: " + genericProductDto.getCategory().getId()));
            } else {
                genericProductDto.setCategory(new GenericCategoryDto());
            }

            if (genericProductDto.getSupplier() != null) {
                Optional<Supplier> supplier = supplierRepository.findById(genericProductDto.getSupplier().getId());
                supplier.ifPresent(existingProduct::setSupplier);
                supplier.orElseThrow(() -> new NotFoundException("Supplier not found by id: " + genericProductDto.getSupplier().getId()));
            } else {
                genericProductDto.setSupplier(new GenericSupplierDto());
            }

            Product savedProduct = productRepository.save(existingProduct);
            genericProductDto.setId(savedProduct.getId());
            genericProductDto.getCategory().setId(savedProduct.getCategory().getId());
            genericProductDto.getSupplier().setId(savedProduct.getSupplier().getId());

            redisTemplate.opsForHash().delete("PRODUCT", id);
            eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_UPDATED));
            return genericProductDto;
        } catch (NotFoundException e) {
            log.error("Error occurred while updating product", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while updating product", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while updating product");
        }
    }

    @Override
    public GenericProductDto deleteProduct(String id) throws NotFoundException {
        try {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Product not found by id: " + id));

            // Remove product from all associated suppliers
            if (product.getSupplier() != null && product.getSupplier().getProducts() != null) {
                product.getSupplier().getProducts().remove(product);
                supplierRepository.save(product.getSupplier());
            }
            productRepository.deleteById(id);

            GenericProductDto genericProductDto = new GenericProductDto();
            genericProductDto.setId(product.getId());
            genericProductDto.setTitle(product.getTitle());
            genericProductDto.setDescription(product.getDescription());
            genericProductDto.setSpecifications(product.getSpecifications());
            genericProductDto.setImageUrl(product.getImageUrl());
            genericProductDto.setPrice(product.getPrice());

            if (product.getCategory() != null) {
                genericProductDto.setCategory(GenericCategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build());
            }

            if (product.getSupplier() != null) {
                genericProductDto.setSupplier(GenericSupplierDto.builder()
                        .id(product.getSupplier().getId())
                        .name(product.getSupplier().getName())
                        .build());
            }

            eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_DELETED));
            return genericProductDto;
        } catch (NotFoundException e) {
            log.error("Error occurred while deleting product", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while deleting product", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while deleting product");
        }
    }

    @Override
    public List<GenericProductDto> getProductsInCategory(String categoryId) throws NotFoundException {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            if (categoryOptional.isEmpty()) {
                throw new NotFoundException("Category not found by id: " + categoryId);
            }
            Category category = categoryOptional.get();
            List<GenericProductDto> genericProductDtos = new ArrayList<>();
            category.getProducts().forEach(product -> {
                GenericProductDto genericProductDto = new GenericProductDto();
                genericProductDto.setId(product.getId());
                genericProductDto.setTitle(product.getTitle());
                genericProductDto.setDescription(product.getDescription());
                genericProductDto.setSpecifications(product.getSpecifications());
                genericProductDto.setImageUrl(product.getImageUrl());
                genericProductDto.setPrice(product.getPrice());
                genericProductDto.setCategory(GenericCategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build());
                genericProductDto.setSupplier(GenericSupplierDto.builder()
                        .id(product.getSupplier().getId())
                        .name(product.getSupplier().getName())
                        .build());
                genericProductDtos.add(genericProductDto);
            });
            return genericProductDtos;
        } catch (NotFoundException e) {
            log.error("Error occurred while fetching products in category", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while fetching products in category", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching products in category");
        }
    }

    @Override
    public List<String> getProductTitles(List<String> categoryIDs) {
        try {
            List<Category> categories = categoryRepository.findAllById(categoryIDs);
            List<Product> products = productRepository.findAllByCategoryIn(categories);
            List<String> titles = new ArrayList<>();

            for (Product p : products) {
                titles.add(p.getTitle());
            }

            return titles;
        } catch (Exception e) {
            log.error("Error occurred while fetching product titles", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error occurred while fetching product titles");
        }
    }
}