package dev.eshan.productservice.services.interfaces;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.exceptions.NotFoundException;

import java.util.List;

public interface CategoryService {
    List<GenericCategoryDto> getAllCategories();
    GenericCategoryDto getCategory(String id) throws NotFoundException;
    GenericCategoryDto createCategory(GenericCategoryDto category);
    GenericCategoryDto updateCategory(String id, GenericCategoryDto category) throws NotFoundException;
    void deleteCategory(String id) throws NotFoundException;
}
