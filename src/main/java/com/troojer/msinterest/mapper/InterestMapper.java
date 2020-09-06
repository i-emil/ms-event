package com.troojer.msinterest.mapper;

import com.troojer.msinterest.dao.InterestEntity;
import com.troojer.msinterest.model.InterestDto;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class InterestMapper {
    public static InterestDto entityToDto(InterestEntity entity) {
        return InterestDto.builder()
                .name(entity.getName()).build();
    }

    public static InterestEntity dtoToEntity(InterestDto dto) {
        return InterestEntity.builder()
                .id(dto.getId())
                .name(dto.getName()).build();
    }

    public static Set<InterestDto> entitiesToDtos(Collection<InterestEntity> entities) {
        return entities.stream().map(InterestMapper::entityToDto).collect(Collectors.toSet());
    }

    public static Set<InterestEntity> dtosToEntities(Collection<InterestDto> dtos) {
        return dtos.stream().map(InterestMapper::dtoToEntity).collect(Collectors.toSet());
    }
}
