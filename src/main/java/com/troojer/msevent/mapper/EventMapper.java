package com.troojer.msevent.mapper;

import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.client.LocationClient;
import com.troojer.msevent.client.ParticipantClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.EventDto;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final LanguageMapper languageMapper;
    private final TagMapper tagMapper;
    private final ParticipantClient participantClient;
    private final LocationClient locationClient;
    private final EventParticipantTypeMapper participantTypeMapper;
    private final BudgetMaper budgetMaper;
    private final ImageClient imageClient;

    public EventMapper(LanguageMapper languageMapper, TagMapper tagMapper, ParticipantClient participantClient, LocationClient locationClient, EventParticipantTypeMapper participantTypeMapper, BudgetMaper budgetMaper, ImageClient imageClient) {
        this.languageMapper = languageMapper;
        this.tagMapper = tagMapper;
        this.participantClient = participantClient;
        this.locationClient = locationClient;
        this.participantTypeMapper = participantTypeMapper;
        this.budgetMaper = budgetMaper;
        this.imageClient = imageClient;
    }

    public EventDto entityToDto(EventEntity entity) {
        return EventDto.builder()
                .id(entity.getId())
                .authorId(entity.getAuthorId())
                .location(locationClient.getLocation(entity.getLocationId()))
                .description(entity.getDescription())
                .cover(entity.getCover() != null ? imageClient.getImageUrl(entity.getCover()) : null)
                .date(EventDateMapper.entityDatesToDto(entity.getStartDate(), entity.getEndDate()))
                .title(entity.getTitle())
                .budget((entity.getBudget() != null) ? budgetMaper.eventToBudgetDto(entity) : null)
                .participantsType(participantTypeMapper.entitiesToDtos(entity.getParticipantsType()))
                .participants(participantClient.getParticipants(entity.getId()))
                .age(AgeDto.builder().min(entity.getMinAge()).max(entity.getMaxAge()).build())
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
                .locationId(dto.getLocation().getId())
                .description(dto.getDescription().strip().toLowerCase())
                .cover(dto.getCover())
                .startDate(EventDateMapper.dtoToStartDate(dto.getDate()))
                .endDate(EventDateMapper.dtoToEndDate(dto.getDate()))
                .title(dto.getTitle().strip().toLowerCase())
                .budget((dto.getBudget() != null) ? dto.getBudget().getAmount() : null)
                .currency((dto.getBudget() != null) ? dto.getBudget().getCurrency().getCode() : null)
                .minAge(dto.getAge().getMin())
                .maxAge(dto.getAge().getMax())
                .build();
        eventEntity.setLanguages(languageMapper.dtoSetToEntitySet(dto.getLanguages(), eventEntity));
        eventEntity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags(), eventEntity));
        eventEntity.setParticipantsType(participantTypeMapper.dtosToEntities(dto.getParticipantsType(), eventEntity));
        return eventEntity;
    }

    public EventEntity updateEntity(EventDto dto, EventEntity entity) {
        if (dto.getLocation() != null && dto.getLocation().getId() != null)
            entity.setLocationId(dto.getLocation().getId());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription().strip().toLowerCase());
        if (dto.getCover() != null) entity.setCover(dto.getCover());
        if (dto.getDate() != null) {
            entity.setStartDate(EventDateMapper.dtoToStartDate(dto.getDate()));
            entity.setEndDate(EventDateMapper.dtoToEndDate(dto.getDate()));
        }
        if (dto.getTitle() != null) entity.setTitle(dto.getTitle().strip().toLowerCase());
        if (dto.getBudget() != null) {
            entity.setBudget(dto.getBudget().getAmount());
            entity.setCurrency(dto.getBudget().getCurrency().getCode());
        }
        if (dto.getTags() != null) entity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags(), entity));
        return entity;
    }

}
