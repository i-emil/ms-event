package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.CategoryEntity;
import com.troojer.msevent.model.CategoryDto;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryMapper {

    public static CategoryDto entityToDto(CategoryEntity entity) {
        return CategoryDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .build();
    }

    public static CategoryEntity dtoToEntity(CategoryDto dto) {
        return CategoryEntity.builder()
                .id(dto.getId())
                .build();
    }

    public static Set<CategoryDto> entitiesToCategorySet(Collection<CategoryEntity> categories) {
        return categories.stream().map(CategoryMapper::entityToDto).collect(Collectors.toSet());
    }

}
