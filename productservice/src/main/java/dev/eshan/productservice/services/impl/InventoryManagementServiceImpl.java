package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GenericSupplierDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.repositories.ProductRepository;
import dev.eshan.productservice.services.interfaces.InventoryManagementService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryManagementServiceImpl implements InventoryManagementService {
    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public InventoryManagementServiceImpl(ProductRepository productRepository,
                                          RedisTemplate<String, Object> redisTemplate) {
        this.productRepository = productRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<GenericProductDto> getLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findLowStockProducts();

        return lowStockProducts.stream().map(product -> {
            GenericProductDto dto = new GenericProductDto();
            dto.setId(product.getId());
            dto.setTitle(product.getTitle());
            dto.setDescription(product.getDescription());
            dto.setSpecifications(product.getSpecifications());
            dto.setImageUrl(product.getImageUrl());
            dto.setPrice(product.getPrice());
            dto.setStockLevel(product.getStockLevel());
            dto.setLowStockThreshold(product.getLowStockThreshold());

            if (product.getCategory() != null) {
                dto.setCategory(GenericCategoryDto.builder()
                        .id(product.getCategory().getId())
                        .name(product.getCategory().getName())
                        .build());
            }

            if (product.getSupplier() != null) {
                dto.setSupplier(GenericSupplierDto.builder()
                        .id(product.getSupplier().getId())
                        .name(product.getSupplier().getName())
                        .build());
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void restockProduct(String id, int quantity) throws NotFoundException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found by id: " + id));
        product.setStockLevel(product.getStockLevel() + quantity);
        productRepository.save(product);
        redisTemplate.opsForHash().delete("PRODUCT", id);
    }
}
