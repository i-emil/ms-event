package com.troojer.msevent.mapper;

import com.troojer.msevent.client.ParticipantClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.enm.EventType;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final LanguageMapper languageMapper;
    private final TagMapper tagMapper;
    private final ParticipantClient participantClient;
    private final EventParticipantTypeMapper participantTypeMapper;

    public EventMapper(LanguageMapper languageMapper, TagMapper tagMapper, ParticipantClient participantClient, EventParticipantTypeMapper participantTypeMapper) {
        this.languageMapper = languageMapper;
        this.tagMapper = tagMapper;
        this.participantClient = participantClient;
        this.participantTypeMapper = participantTypeMapper;
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
                .participantsType(participantTypeMapper.entitiesToDtos(entity.getParticipantsType()))
                .participants(participantClient.getParticipants(entity.getId()))
                .age(AgeDto.builder().min(entity.getMinAge()).max(entity.getMaxAge()).build())
                .type(entity.getType().toString())
                .status(entity.getStatus())
                .languages(languageMapper.entitySetToDtoSet(entity.getLanguages()))
                .tags(tagMapper.entitySetToDtoSet(entity.getTags()))
                .build();
    }

    public EventDto entityToDtoWithKey(EventEntity eventEntity, String key) {
        EventDto dto = entityToDto(eventEntity);
        dto.setKey(key);
        return dto;
    }

    public EventEntity createEntity(EventDto dto, String authorId) {
        EventEntity eventEntity = EventEntity.builder()
                .authorId(authorId)
                .locationId(dto.getLocationId())
                .description(dto.getDescription().strip().toLowerCase())
                .startDate(EventDateMapper.dtoToStartDate(dto.getDate()))
                .endDate(EventDateMapper.dtoToEndDate(dto.getDate()))
                .title(dto.getTitle().strip().toLowerCase())
                .budget(dto.getBudget())
                .minAge(dto.getAge().getMin())
                .maxAge(dto.getAge().getMax())
                .type(EventType.valueOf(dto.getType()))
                .build();
        eventEntity.setLanguages(languageMapper.dtoSetToEntitySet(dto.getLanguages(), eventEntity));
        eventEntity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags(), eventEntity));
        eventEntity.setParticipantsType(participantTypeMapper.dtosToEntities(dto.getParticipantsType(), eventEntity));
        return eventEntity;
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
        if (dto.getTags() != null) entity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags(), entity));
        return entity;
    }

}
