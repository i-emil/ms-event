package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.model.EventParticipantTypeDto;
import com.troojer.msevent.model.enm.ParticipantType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
public class EventParticipantTypeMapper {

    public EventParticipantTypeDto entityToDto(EventParticipantTypeEntity entity) {
        return EventParticipantTypeDto.builder()
                .accepted(entity.getAccepted())
                .total(entity.getTotal())
                .build();
    }

    public EventParticipantTypeEntity dtoToEntity(EventParticipantTypeDto dto, ParticipantType type, EventEntity event) {
        return EventParticipantTypeEntity.builder()
                .type(type)
                .event(event)
                .accepted(dto.getAccepted())
                .total(dto.getTotal())
                .build();
    }

    public Map<ParticipantType, EventParticipantTypeDto> entitiesToDtos(Map<ParticipantType, EventParticipantTypeEntity> entities) {
        return entities.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, y -> entityToDto(y.getValue())));
    }

    public Map<ParticipantType, EventParticipantTypeEntity> dtosToEntities(Map<ParticipantType, EventParticipantTypeDto> dtos, EventEntity event) {
        return dtos.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, y -> dtoToEntity(y.getValue(), y.getKey(), event)));
    }
}
