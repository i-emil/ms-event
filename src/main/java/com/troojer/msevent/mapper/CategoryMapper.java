package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.CategoryEntity;
import com.troojer.msevent.model.CategoryDto;

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
                .title(dto.getTitle())
                .build();
    }

}
