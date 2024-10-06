package dev.eshan.productservice.services.impl;

import dev.eshan.productservice.dtos.GenericCategoryDto;
import dev.eshan.productservice.dtos.GenericProductDto;
import dev.eshan.productservice.events.CategoryEvent;
import dev.eshan.productservice.events.EventName;
import dev.eshan.productservice.exceptions.NotFoundException;
import dev.eshan.productservice.models.Category;
import dev.eshan.productservice.models.Product;
import dev.eshan.productservice.repositories.CategoryRepository;
import dev.eshan.productservice.services.interfaces.CategoryService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.categoryRepository = categoryRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public List<GenericCategoryDto> getAllCategories() {
        List<Category> categoryList = categoryRepository.findAll();
        return categoryList.stream().map(category ->
                GenericCategoryDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public GenericCategoryDto getCategory(String id) throws NotFoundException {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        Category category = categoryOptional.orElseThrow(() -> new NotFoundException("Category not found by id: " + id));
        List<GenericProductDto> products = new ArrayList<>();
        for (Product product : category.getProducts()) {
            GenericProductDto p = new GenericProductDto();
            p.setTitle(product.getTitle());
            p.setDescription(product.getDescription());
            products.add(p);
        }
        return GenericCategoryDto.builder()
                .name(category.getName())
                .products(products)
                .build();
    }

    @Override
    public GenericCategoryDto createCategory(GenericCategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        GenericCategoryDto savedCategoryDto = new GenericCategoryDto();
        savedCategoryDto.setId(savedCategory.getId());
        savedCategoryDto.setName(savedCategory.getName());
        eventPublisher.publishEvent(getCategoryEvent(savedCategoryDto, EventName.CATEGORY_CREATED));
        return savedCategoryDto;
    }

    @Override
    public GenericCategoryDto updateCategory(String id, GenericCategoryDto categoryDto) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found by id: " + id));
        category.setName(categoryDto.getName());
        Category savedCategory = categoryRepository.save(category);
        GenericCategoryDto savedCategoryDto = new GenericCategoryDto();
        savedCategoryDto.setId(savedCategory.getId());
        savedCategoryDto.setName(savedCategory.getName());
        eventPublisher.publishEvent(getCategoryEvent(savedCategoryDto, EventName.CATEGORY_UPDATED));
        return savedCategoryDto;
    }

    @Override
    public void deleteCategory(String id) throws NotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found by id: " + id));
        eventPublisher.publishEvent(getCategoryEvent(
                GenericCategoryDto.builder().id(category.getId()).name(category.getName()).build(),
                EventName.CATEGORY_DELETED));
        categoryRepository.delete(category);
    }

    private CategoryEvent getCategoryEvent(GenericCategoryDto genericCategoryDto, EventName eventName) {
        CategoryEvent categoryEvent = new CategoryEvent();
        categoryEvent.setEventName(eventName);
        categoryEvent.setCategory(genericCategoryDto);
        return categoryEvent;
    }
}
