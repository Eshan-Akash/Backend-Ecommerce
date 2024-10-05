package dev.eshan.productservice.controllers;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.factory.CategoryServiceFactory;
import dev.eshan.productservice.services.interfaces.CategoryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Value("${app.serviceProviderId}")
    private String serviceProviderId;
    private final CategoryServiceFactory categoryServiceFactory;
    public CategoryController(CategoryServiceFactory categoryServiceFactory) {
        this.categoryServiceFactory = categoryServiceFactory;
    }

    @GetMapping
    public List<GenericCategoryDto> getAllCategories() throws Exception {
        CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
        return categoryService.getAllCategories();
    }

    @GetMapping("/{id}")
    public GenericCategoryDto getCategoryById(@PathVariable String id) throws Exception {
        CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
        return categoryService.getCategory(id);
    }

    @PostMapping
    public GenericCategoryDto createCategory(@RequestBody GenericCategoryDto category) throws Exception {
        // 1. Create a new category
        // 2. CATEGORY_CREATED event is published
        CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public GenericCategoryDto updateCategory(@PathVariable String id, @RequestBody GenericCategoryDto category)
            throws Exception {
        // 1. Update the category
        // 2. CATEGORY_UPDATED event is published
        CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/{id}")
    public void deleteCategory(@PathVariable String id) throws Exception {
        // 1. Delete the category
        // 2. CATEGORY_DELETED event is published
        CategoryService categoryService = categoryServiceFactory.create(serviceProviderId);
        categoryService.deleteCategory(id);
    }
}
