package com.troojer.msevent.service;

import com.troojer.msevent.dao.CategoryEntity;
import com.troojer.msevent.model.CategoryDto;

import java.util.Set;

public interface CategoryService {

    CategoryDto getCategoryDto(Long categoryId);

    CategoryEntity getCategoryEntity(Long categoryId);

    Set<CategoryDto> getCategories();
}
