package com.troojer.msevent.service.impl;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SavedEventEntity;
import com.troojer.msevent.dao.repository.SavedEventRepository;
import com.troojer.msevent.mapper.SavedEventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.SavedEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SavedEventServiceImpl implements SavedEventService {

    private final InnerEventService innerEventService;
    private final SavedEventRepository savedEventRepository;
    private final AccessCheckerUtil accessChecker;
    private final SavedEventMapper savedEventMapper;

    @Override
    public void saveEvent(String eventKey) {
        EventEntity eventEntity = innerEventService.getEventEntity(eventKey).orElseThrow(() -> new NotFoundException("event.event.notFound"));
        SavedEventEntity savedEventEntity = getOrCreateNewSavedEventEntity(eventEntity);
        savedEventRepository.save(savedEventEntity);
    }

    @Override
    public Page<EventDto> getSavedEvents(Pageable pageable) {
        Page<SavedEventEntity> savedEventEntityPage = savedEventRepository.getAllByUserIdAndDeleted(accessChecker.getUserId(), false, pageable);
        return savedEventEntityPage.map(savedEventMapper::savedEventEntityToEventDto);
    }

    @Override
    public void deleteSavedEvent(String eventKey) {
        SavedEventEntity savedEvent = savedEventRepository.getByUserAndEventKey(accessChecker.getUserId(), eventKey).orElseThrow(() -> new NotFoundException("event.savedEvent.notFound"));
        savedEvent.setDeleted(true);
        savedEventRepository.save(savedEvent);
    }

    private SavedEventEntity getOrCreateNewSavedEventEntity(EventEntity eventEntity) {
        Optional<SavedEventEntity> savedEventEntityOpt = savedEventRepository.getByUserAndEventKey(accessChecker.getUserId(), eventEntity.getKey());
        SavedEventEntity savedEventEntity;
        if (savedEventEntityOpt.isPresent()) {
            savedEventEntity = savedEventEntityOpt.get();
            savedEventEntity.setDeleted(false);
        } else {
            savedEventEntity = savedEventMapper.eventEntityToSavedEventEntity(eventEntity);
        }
        return savedEventEntity;
    }
}
