package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {

    @Override
    public List<GenericProductDto> getProducts() {
        return null;
    }

    @Override
    public GenericProductDto getProductById(String id, Long userIdTryingToAccess) throws NotFoundException {
        return null;
    }

    @Override
    public List<GenericProductDto> getProductsInCategory(String categoryId) throws NotFoundException {
        return null;
    }

    @Override
    public GenericProductDto createProduct(GenericProductDto genericProductDto) {
        return null;
    }

    @Override
    public GenericProductDto updateProduct(String id, GenericProductDto genericProductDto) throws NotFoundException {
        return null;
    }

    @Override
    public GenericProductDto deleteProduct(String id) throws NotFoundException {
        return null;
    }

    @Override
    public List<String> getProductTitles(List<String> categoryIDs) {
        return null;
    }
}