package com.troojer.mstag.mapper;

import com.troojer.mstag.dao.TagEntity;
import com.troojer.mstag.model.TagDto;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TagMapper {
    public static TagDto entityToDto(TagEntity entity) {
        return TagDto.builder()
                .id(entity.getId())
                .value(entity.getValue()).build();
    }

    public static TagEntity dtoToEntity(TagDto dto) {
        return TagEntity.builder()
                .value(dto.getValue()).build();
    }

    public static Set<String> dtosToStringSet(Collection<TagDto> dtos){
        return dtos.stream().map(TagDto::getValue).collect(Collectors.toSet());
    }
    public static List<TagDto> entitiesToDtos(Collection<TagEntity> entities) {
        return entities.stream().map(TagMapper::entityToDto).collect(Collectors.toList());
    }

    public static Set<TagEntity> dtosToEntities(Collection<TagDto> dtos) {
        return dtos.stream().map(TagMapper::dtoToEntity).collect(Collectors.toSet());
    }
}
