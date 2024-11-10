package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.factory.CategoryServiceFactory;
import dev.eshan.productservice.services.interfaces.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static dev.eshan.productservice.utils.Utils.ERROR_MESSAGE;

@RestController
@RequestMapping("/api/v1/categories")
@Slf4j
public class CategoryController {
    @Value("${app.serviceProviderId}")
    private String serviceProviderId;
    private final CategoryServiceFactory categoryServiceFactory;
    public CategoryController(CategoryServiceFactory categoryServiceFactory) {
        this.categoryServiceFactory = categoryServiceFactory;
    }

    @GetMapping
    public List<GenericCategoryDto> getAllCategories() throws Exception {
        try {
            CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
            return categoryService.getAllCategories();
        } catch (Exception e) {
            log.error("Error occurred while fetching categories", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @GetMapping("/{id}")
    public GenericCategoryDto getCategoryById(@PathVariable String id) throws Exception {
        try {
            CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
            return categoryService.getCategory(id);
        } catch (Exception e) {
            log.error("Error occurred while fetching category with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @PostMapping
    public GenericCategoryDto createCategory(@RequestBody GenericCategoryDto category) throws Exception {
        if (category == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is required");
        }
        if (category.getName() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }
        try {
            CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
            return categoryService.createCategory(category);
        } catch (Exception e) {
            log.error("Error occurred while creating category", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @PutMapping("/{id}")
    public GenericCategoryDto updateCategory(@PathVariable String id, @RequestBody GenericCategoryDto category)
            throws Exception {
        try {
            CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
            return categoryService.updateCategory(id, category);
        } catch (Exception e) {
            log.error("Error occurred while updating category with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) throws Exception {
        try {
            CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
            categoryService.deleteCategory(id);
        } catch (Exception e) {
            log.error("Error occurred while deleting category with id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_MESSAGE);
        }
    }
}
