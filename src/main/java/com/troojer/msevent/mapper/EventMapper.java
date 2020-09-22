package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.EventAgeDto;
import com.troojer.msevent.model.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final LanguageMapper languageMapper;
    private final TagMapper tagMapper;

    public EventMapper(LanguageMapper languageMapper, TagMapper tagMapper) {
        this.languageMapper = languageMapper;
        this.tagMapper = tagMapper;
    }

    public EventDto entityToDto(EventEntity entity) {
        return EventDto.builder()
                .id(entity.getId())
                .authorId(entity.getAuthorId())
                .locationId(entity.getLocationId())
                .description(entity.getDescription())
                .date(EventDateMapper.entityDatesToDto(entity.getStartDate(), entity.getEndDate()))
                .title(entity.getTitle())
                .budget(entity.getBudget())
                .personCount(EventPersonCountMapper.entityToDto(entity))
                .age(EventAgeDto.builder().min(entity.getMinAge()).max(entity.getMaxAge()).build())
                .watched(entity.getWatched())
                .status(entity.getStatus())
                .languages(languageMapper.entitySetToDtoSet(entity.getLanguages()))
                .tags(tagMapper.entitySetToDtoSet(entity.getTags()))
                .category(CategoryMapper.entityToDto(entity.getCategory()))
                .build();
    }

    public EventEntity createEntity(EventDto dto) {
        return EventEntity.builder()
                .locationId(dto.getLocationId())
                .description(dto.getDescription().strip().toLowerCase())
                .startDate(EventDateMapper.dtoToStartDate(dto.getDate()))
                .endDate(EventDateMapper.dtoToEndDate(dto.getDate()))
                .title(dto.getTitle().strip().toLowerCase())
                .budget(dto.getBudget())
                .minAge(dto.getAge().getMin())
                .maxAge(dto.getAge().getMax())
                .malePersonCount(EventPersonCountMapper.dtoToMaleCount(dto.getPersonCount()))
                .femalePersonCount(EventPersonCountMapper.dtoToFemaleCount(dto.getPersonCount()))
                .allPersonCount(EventPersonCountMapper.dtoToAllCount(dto.getPersonCount()))
                .category(CategoryMapper.dtoToEntity(dto.getCategory()))
                .languages(languageMapper.dtoSetToEntitySet(dto.getLanguages()))
                .tags(tagMapper.dtoSetToEntitySet(dto.getTags()))
                .build();
    }

    public EventEntity updateEntity(EventDto dto, EventEntity entity) {
        if (dto.getLocationId() != null) entity.setLocationId(dto.getLocationId());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription().strip().toLowerCase());
        if (dto.getDate() != null) {
            entity.setStartDate(EventDateMapper.dtoToStartDate(dto.getDate()));
            entity.setEndDate(EventDateMapper.dtoToEndDate(dto.getDate()));
        }
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle().strip().toLowerCase());
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
        if (dto.getLanguages() != null) entity.setLanguages(languageMapper.dtoSetToEntitySet(dto.getLanguages()));
        if (dto.getTags() != null) entity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags()));
        return entity;
    }

}
