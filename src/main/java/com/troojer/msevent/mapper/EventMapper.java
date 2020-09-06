package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.EventAgeDto;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.LanguageDto;

import java.util.Set;

public class EventMapper {

    public static EventDto entityToDto(EventEntity entity, Set<LanguageDto> languageSet) {
        return EventDto.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .locationId(entity.getLocationId())
                .description(entity.getDescription())
                .date(EventDateMapper.entityDatesToDto(entity.getStartDate(), entity.getEndDate()))
                .title(entity.getTitle())
                .budget(entity.getBudget())
                .personCount(EventPersonCountMapper.entityToDto(entity))
                .age(EventAgeDto.builder().min(entity.getMinAge()).max(entity.getMaxAge()).build())
                .status(entity.getStatus())
                .languages(languageSet)
                .category(CategoryMapper.entityToDto(entity.getCategory()))
                .build();
    }

    public static EventEntity dtoToEntityForCreate(EventDto dto) {
        return EventEntity.builder()
                .locationId(dto.getLocationId())
                .description(dto.getDescription().strip())
                .startDate(EventDateMapper.dtoToStartDate(dto.getDate()))
                .endDate(EventDateMapper.dtoToEndDate(dto.getDate()))
                .title(dto.getTitle().strip())
                .budget(dto.getBudget())
                .minAge(dto.getAge().getMin())
                .maxAge(dto.getAge().getMax())
                .malePersonCount(EventPersonCountMapper.dtoToMaleCount(dto.getPersonCount()))
                .femalePersonCount(EventPersonCountMapper.dtoToFemaleCount(dto.getPersonCount()))
                .allPersonCount(EventPersonCountMapper.dtoToAllCount(dto.getPersonCount()))
                .category(CategoryMapper.dtoToEntity(dto.getCategory()))
                .build();
    }

    public static EventEntity updateEntity(EventDto dto, EventEntity entity) {
        if (dto.getLocationId() != null) entity.setLocationId(dto.getLocationId());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getDate() != null) {
            entity.setStartDate(EventDateMapper.dtoToStartDate(dto.getDate()));
            entity.setEndDate(EventDateMapper.dtoToEndDate(dto.getDate()));
        }
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle());
        if (dto.getBudget() != null) entity.setBudget(dto.getBudget());
        if (dto.getAge() != null) {
            entity.setMinAge(dto.getAge().getMin());
            entity.setMaxAge(dto.getAge().getMax());
        }
        if (dto.getPersonCount() != null) {
            entity.setMalePersonCount(EventPersonCountMapper.dtoToMaleCount(dto.getPersonCount()));
            entity.setFemalePersonCount(EventPersonCountMapper.dtoToFemaleCount(dto.getPersonCount()));
            entity.setAllPersonCount(EventPersonCountMapper.dtoToAllCount(dto.getPersonCount()));
        }
        if (dto.getCategory() != null) entity.setCategory(CategoryMapper.dtoToEntity(dto.getCategory()));
        return entity;
    }

}
