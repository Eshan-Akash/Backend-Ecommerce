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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;
    private final ApplicationEventPublisher eventPublisher;

    ProductServiceImpl(ProductRepository productRepository,
                       CategoryRepository categoryRepository,
                       SupplierRepository supplierRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<GenericProductDto> getProducts() {
        Optional<List<Product>> products = Optional.of(productRepository.findAll());
        List<GenericProductDto> genericProductDtos = new ArrayList<>();
        if (products.isPresent()) {
            for (Product product : products.get()) {
                GenericProductDto genericProductDto = new GenericProductDto();
                genericProductDto.setId(product.getId());
                genericProductDto.setTitle(product.getTitle());
                genericProductDto.setDescription(product.getDescription());
                genericProductDto.setSpecifications(product.getSpecifications());
                genericProductDto.setImageUrl(product.getImageUrl());
                genericProductDto.setPrice(product.getPrice());
                genericProductDtos.add(genericProductDto);
                genericProductDto.setCategory(GenericCategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build());
            }
        }
        return genericProductDtos;
    }

    @Override
    public GenericProductDto getProductById(String id, Long userIdTryingToAccess) throws NotFoundException {
        return getProductFromStoreById(id);
    }

    @Transactional
    public GenericProductDto getProductFromStoreById(String id) throws NotFoundException {
        Optional<Product> productOptional = productRepository.findById(id);
        Product product = productOptional.orElseThrow(() -> new NotFoundException("Product not found by id: " + id));

        GenericProductDto genericProductDto = new GenericProductDto();
        genericProductDto.setId(product.getId());
        genericProductDto.setTitle(product.getTitle());
        genericProductDto.setDescription(product.getDescription());
        genericProductDto.setSpecifications(product.getSpecifications());
        genericProductDto.setImageUrl(product.getImageUrl());
        genericProductDto.setPrice(product.getPrice());

        Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
        if (category.isPresent()) {
            // don't set products here to avoid infinite recursion // Since the List of products are fetched lazily by default in JPA repositories
            genericProductDto.setCategory(GenericCategoryDto.builder()
                    .id(category.get().getId())
                    .name(category.get().getName())
                    .build());
        }
        return genericProductDto;
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto genericProductDto) throws NotFoundException {
        Product product = new Product();
        product.setTitle(genericProductDto.getTitle());
        product.setDescription(genericProductDto.getDescription());
        product.setSpecifications(genericProductDto.getSpecifications());
        product.setImageUrl(genericProductDto.getImageUrl());
        product.setPrice(genericProductDto.getPrice());

        // Handle category
        Category category = null;
        if (genericProductDto.getCategory() != null && genericProductDto.getCategory().getId() != null) {
            category = categoryRepository.findById(genericProductDto.getCategory().getId())
                    .orElseThrow(() -> new NotFoundException("Category not found by id: " + genericProductDto.getCategory().getId()));
        } else {
            throw new NotFoundException("Category not found by id: " + genericProductDto.getCategory().getId());
        }
        product.setCategory(category);

        // Handle supplier
        Supplier supplier = null;
        if (genericProductDto.getSupplier() != null && genericProductDto.getSupplier().getId() != null) {
            // Assuming you have a SupplierRepository to find existing suppliers
            supplier = supplierRepository.findById(genericProductDto.getSupplier().getId())
                    .orElseThrow(() -> new NotFoundException("Supplier not found by id: " + genericProductDto.getSupplier().getId()));
        } else {
            throw new NotFoundException("Supplier not found by id: " + genericProductDto.getSupplier() != null ?
                    genericProductDto.getSupplier().getId() : null);
        }
        product.setSupplier(supplier);

        // Save the product
        Product savedProduct = productRepository.save(product);

        // Update the DTO with IDs
        genericProductDto.setId(savedProduct.getId());
        genericProductDto.getCategory().setId(savedProduct.getCategory().getId());
        genericProductDto.getSupplier().setId(savedProduct.getSupplier().getId());

        // Publish the product creation event
        eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_CREATED));
        return genericProductDto;
    }

    private ProductEvent getProductEvent(GenericProductDto genericProductDto, EventName eventName) {
        ProductEvent productEvent = new ProductEvent();
        productEvent.setEventName(eventName);
        productEvent.setProduct(genericProductDto);
        return productEvent;
    }

    @Override
    public GenericProductDto updateProduct(String id, GenericProductDto genericProductDto) throws NotFoundException {
        Optional<Product> existingProductOptional = productRepository.findById(id);
        if (existingProductOptional.isEmpty()) {
            throw new NotFoundException("Product not found by id: " + id);
        }

        Product existingProduct = existingProductOptional.get();

        // Update product fields
        existingProduct.setTitle(genericProductDto.getTitle());
        existingProduct.setDescription(genericProductDto.getDescription());
        existingProduct.setSpecifications(genericProductDto.getSpecifications());
        existingProduct.setImageUrl(genericProductDto.getImageUrl());
        existingProduct.setPrice(genericProductDto.getPrice());

        // Handle category update
        if (genericProductDto.getCategory() != null && genericProductDto.getCategory().getId() != null) {
            Optional<Category> categoryOptional = categoryRepository.findById(genericProductDto.getCategory().getId());
            categoryOptional.ifPresent(existingProduct::setCategory);
            categoryOptional.orElseThrow(() -> new NotFoundException("Category not found by id: " + genericProductDto.getCategory().getId()));
        } else {
            genericProductDto.setCategory(new GenericCategoryDto());
        }

        // Handle supplier update
        if (genericProductDto.getSupplier() != null) {
            Optional<Supplier> supplier = supplierRepository.findById(genericProductDto.getSupplier().getId());
            supplier.ifPresent(existingProduct::setSupplier);
            supplier.orElseThrow(() -> new NotFoundException("Supplier not found by id: " + genericProductDto.getSupplier().getId()));
        } else {
            genericProductDto.setSupplier(new GenericSupplierDto());
        }

        // Save the updated product
        Product savedProduct = productRepository.save(existingProduct);
        genericProductDto.setId(savedProduct.getId());
        genericProductDto.getCategory().setId(savedProduct.getCategory().getId());
        genericProductDto.getSupplier().setId(savedProduct.getSupplier().getId());

        // Publish the product update event
        eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_UPDATED));
        return genericProductDto;
    }

    @Override
    public GenericProductDto deleteProduct(String id) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found by id: " + id));

        // Remove product from all associated suppliers
        if (product.getSupplier() != null && product.getSupplier().getProducts() != null) {
            product.getSupplier().getProducts().remove(product);
            supplierRepository.save(product.getSupplier());
        }

        // Now delete the product safely
        productRepository.deleteById(id);

        // Build and return the response DTO
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

        // Publish PRODUCT_DELETED event
        eventPublisher.publishEvent(getProductEvent(genericProductDto, EventName.PRODUCT_DELETED));

        // Return the deleted product details
        return genericProductDto;
    }

    @Override
    public List<GenericProductDto> getProductsInCategory(String categoryId) throws NotFoundException {
        return null;
    }

    @Override
    public List<String> getProductTitles(List<String> categoryIDs) {
        return null;
    }
}