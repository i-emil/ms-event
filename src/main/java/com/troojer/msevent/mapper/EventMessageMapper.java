package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventMessageEntity;
import com.troojer.msevent.model.EventMessageDto;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventMessageMapper {

    private final AccessCheckerUtil accessChecker;

    public EventMessageMapper(AccessCheckerUtil accessChecker) {
        this.accessChecker = accessChecker;
    }

    public EventMessageDto entityToDto(EventMessageEntity entity) {
        return EventMessageDto.builder()
                .message(entity.getMessage())
                .userId(entity.getUserId())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public EventMessageEntity dtoToEntity(EventMessageDto dto, Long eventId) {
        return EventMessageEntity.builder()
                .message(dto.getMessage().strip())
                .userId(accessChecker.getUserId())
                .eventId(eventId)
                .build();
    }

    public List<EventMessageDto> entitiesToDtos(List<EventMessageEntity> entities) {
        return entities.stream().map(this::entityToDto).collect(Collectors.toList());
    }
}
