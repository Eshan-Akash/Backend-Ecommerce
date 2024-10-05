package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GetProductTitlesRequestDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(@Qualifier("productServiceImpl") ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    public List<GenericProductDto> getAllProducts() {
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public GenericProductDto getProductById(@PathVariable("id") String id) throws NotFoundException {
        GenericProductDto productDto = productService.getProductById(id, null);
        if (productDto == null) {
            throw new NotFoundException("Product not found");
        }
        return productDto;
    }

    @PostMapping
    public GenericProductDto createProduct(@RequestBody GenericProductDto product) {
        // 1. Create a new product
        // 2. PRODUCT_CREATED event is published
        return productService.createProduct(product);
    }

    @PutMapping("/{id}")
    public GenericProductDto updateProductById(@PathVariable("id") String id, @RequestBody GenericProductDto product)
            throws NotFoundException {
        // 1. Update the product
        // 2. PRODUCT_UPDATED event is published
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GenericProductDto> deleteProductById(@PathVariable String id) throws NotFoundException {
        // 1. Delete the product
        // 2. PRODUCT_DELETED event is published
        return new ResponseEntity<>(productService.deleteProduct(id),
                HttpStatus.OK);
    }

    @GetMapping("/category/{id}")
    public List<GenericProductDto> getProductsInCategory(@PathVariable("id") String id) throws NotFoundException {
        return productService.getProductsInCategory(id);
    }

    @GetMapping("/titles/")
    public List<String> getProductTitles(@RequestBody GetProductTitlesRequestDto requestDto) {
        List<String> categoryIDs = requestDto.getIds();
        return productService.getProductTitles(categoryIDs);
    }
}
