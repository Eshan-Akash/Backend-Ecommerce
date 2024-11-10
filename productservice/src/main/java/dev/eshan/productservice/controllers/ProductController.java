package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.dtos.GetProductTitlesRequestDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static dev.eshan.productservice.utils.Utils.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/v1/products")
@Slf4j
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
        try {
            GenericProductDto productDto = productService.getProductById(id);
            if (productDto == null) {
                throw new NotFoundException("Product not found");
            }
            return productDto;
        } catch (NotFoundException e) {
            log.error("Product not found with id: {}", id, e);
            throw new NotFoundException("Product not found");
        } catch (Exception e) {
            log.error("Error occurred while fetching product with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @PostMapping
    public GenericProductDto createProduct(@RequestBody GenericProductDto product) throws NotFoundException {
        try {
            return productService.createProduct(product);
        } catch (Exception e) {
            log.error("Error occurred while creating product", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @PutMapping("/{id}")
    public GenericProductDto updateProductById(@PathVariable("id") String id, @RequestBody GenericProductDto product)
            throws NotFoundException {
        try {
            return productService.updateProduct(id, product);
        } catch (Exception e) {
            log.error("Error occurred while updating product with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public GenericProductDto deleteProductById(@PathVariable String id) throws NotFoundException {
        try {
            return productService.deleteProduct(id);
        } catch (Exception e) {
            log.error("Error occurred while deleting product with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @GetMapping("/category/{id}")
    public List<GenericProductDto> getProductsInCategory(@PathVariable("id") String id) throws NotFoundException {
        try {
            return productService.getProductsInCategory(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching products in category with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @GetMapping("/titles/")
    public List<String> getProductTitles(@RequestBody GetProductTitlesRequestDto requestDto) {
        try {
            List<String> categoryIDs = requestDto.getIds();
            return productService.getProductTitles(categoryIDs);
        } catch (Exception e) {
            log.error("Error occurred while fetching product titles", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }
}
