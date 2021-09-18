package com.troojer.msevent.mapper;

import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.InvitingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final LanguageMapper languageMapper;
    private final TagMapper tagMapper;
    private final EventParticipantTypeMapper participantTypeMapper;
    private final BudgetMaper budgetMaper;
    private final ImageClient imageClient;

    public EventDto simpleToDto(SimpleEvent simpleEvent) {
        return EventDto
                .builder()
                .key(simpleEvent.getKey())
                .title(simpleEvent.getTitle())
                .description(simpleEvent.getDescription())
                .cover(imageClient.getImageUrl(simpleEvent.getCover()))
                .startDate(DatesMapper.entityToDto(simpleEvent.getStartDate()))
                .duration(simpleEvent.getDuration())
                .participantsType(participantTypeMapper.entitiesToDtos(simpleEvent.getParticipantsType()))
                .status(simpleEvent.getStatus())
                .locationId(simpleEvent.getLocationId())
                .isJoinInAppEnough(simpleEvent.getIsJoinInAppEnough())
                .build();
    }

    public EventDto entityToDto(EventEntity entity) {
        return EventDto.builder()
                .key(entity.getKey())
                .authorId(entity.getAuthorId())
                .locationId(entity.getLocationId())
                .description(entity.getDescription())
                .cover(entity.getCover() != null ? imageClient.getImageUrl(entity.getCover()) : null)
                .startDate(DatesMapper.entityToDto(entity.getStartDate()))
                .duration(entity.getDuration())
                .title(entity.getTitle())
                .budget((entity.getBudget() != null) ? budgetMaper.eventToBudgetDto(entity) : null)
                .participantsType(participantTypeMapper.entitiesToDtos(entity.getParticipantsType()))
                .age(AgeDto.builder().min(entity.getMinAge()).max(entity.getMaxAge()).build())
                .status(entity.getStatus())
                .languages(languageMapper.entitySetToDtoSet(entity.getLanguages()))
                .tags(tagMapper.entitySetToDtoSet(entity.getTags()))
                .isJoinInAppEnough(entity.getIsJoinInAppEnough())
                .source(entity.getSource())
                .build();
    }

    public EventDto entityToDtoForAuthor(EventEntity entity) {
        EventDto eventDto = entityToDto(entity);
        eventDto.setInviting(InvitingDto.builder().active(entity.isInviteActive()).key(entity.getInviteKey()).password(entity.getInvitePassword()).build());
        return eventDto;
    }

    public EventEntity createEntity(EventDto dto, String authorId) {
        EventEntity eventEntity = EventEntity.builder()
                .authorId(authorId)
                .locationId(dto.getLocationId())
                .description(dto.getDescription().strip().toLowerCase())
                .cover(dto.getCover())
                .startDate(DatesMapper.dtoToEntity(dto.getStartDate()))
                .duration(dto.getDuration())
                .title(dto.getTitle().strip().toLowerCase())
                .budget((dto.getBudget() != null) ? dto.getBudget().getAmount() : null)
                .currency((dto.getBudget() != null) ? dto.getBudget().getCurrency().getCode() : null)
                .minAge((dto.getAge() != null) ? dto.getAge().getMin() : null)
                .maxAge((dto.getAge() != null) ? dto.getAge().getMax() : null)
                .isJoinInAppEnough(dto.getIsJoinInAppEnough())
                .build();
        eventEntity.setLanguages(languageMapper.dtoSetToEntitySet(dto.getLanguages(), eventEntity));
        eventEntity.setTags(tagMapper.dtoSetToEntitySet(dto.getTags(), eventEntity));
        eventEntity.setParticipantsType(participantTypeMapper.dtosToEntities(dto.getParticipantsType(), eventEntity));
        setInviting(eventEntity, dto);
        return eventEntity;
    }

    public List<EventDto> simpleEventsToDtos(List<SimpleEvent> simpleEvents) {
        return simpleEvents.stream().map(this::simpleToDto).collect(Collectors.toList());
    }

    public List<EventDto> entitiesToDtos(List<EventEntity> eventEntityList) {
        return eventEntityList.stream().map(this::entityToDto).collect(Collectors.toList());
    }

    private void setInviting(EventEntity eventEntity, EventDto eventDto) {
        if (eventDto.getInviting() != null) {
            eventEntity.setInviteActive(eventDto.getInviting().isActive());
            eventEntity.setInvitePassword(eventDto.getInviting().getPassword());
        }
    }
}
