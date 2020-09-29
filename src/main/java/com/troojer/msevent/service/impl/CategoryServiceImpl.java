package com.troojer.msevent.service.impl;

import com.troojer.msevent.dao.CategoryEntity;
import com.troojer.msevent.dao.repository.CategoryRepository;
import com.troojer.msevent.mapper.CategoryMapper;
import com.troojer.msevent.model.CategoryDto;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto getCategoryDto(Long categoryId) {
        return CategoryMapper.entityToDto(getCategoryEntity(categoryId));
    }

    @Override
    public CategoryEntity getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(()
                -> new NotFoundException("event.category.notFound"));
    }

    @Override
    public Set<CategoryDto> getCategories() {
        return CategoryMapper.entitiesToCategorySet(categoryRepository.findAll());
    }
}
