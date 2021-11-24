package com.troojer.msevent.mapper;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SavedEventEntity;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SavedEventMapper {

    private final AccessCheckerUtil accessChecker;
    private final EventMapper eventMapper;

    public SavedEventEntity eventEntityToSavedEventEntity(EventEntity eventEntity) {
        return SavedEventEntity.builder().userId(accessChecker.getUserId()).event(eventEntity).build();
    }

    public EventDto savedEventEntityToEventDto(SavedEventEntity savedEventEntity) {
        return eventMapper.entityToDto(savedEventEntity.getEvent());
    }
}
