package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.services.interfaces.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Override
    public GenericCategoryDto getCategory(String id) throws NotFoundException {
        return null;
    }

    @Override
    public List<GenericCategoryDto> getAllCategories() {
        return null;
    }

    @Override
    public GenericCategoryDto createCategory(GenericCategoryDto category) {
        return null;
    }

    @Override
    public GenericCategoryDto updateCategory(String id, GenericCategoryDto category) throws NotFoundException {
        return null;
    }

    @Override
    public void deleteCategory(String id) throws NotFoundException {

    }
}
