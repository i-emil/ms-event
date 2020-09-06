package com.troojer.msevent.service.impl;


import ch.qos.logback.classic.Logger;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.Status;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.CategoryService;
import com.troojer.msevent.service.EventLanguageService;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

import static com.troojer.msevent.util.ToolUtil.getMessage;


@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventLanguageService eventLanguageService;
    private final CategoryService categoryService;
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private AccessCheckerUtil accessChecker;


    public EventServiceImpl(EventRepository eventRepository, EventLanguageServiceImpl languageService, CategoryService categoryService, AccessCheckerUtil accessChecker) {
        this.eventRepository = eventRepository;
        this.eventLanguageService = languageService;
        this.categoryService = categoryService;
        this.accessChecker = accessChecker;
    }

    @Override
    public EventDto getUserEvent(Long eventId) {
        EventEntity eventEntity = getEventEntity(eventId);
        if (!accessChecker.isUserId(eventEntity.getUserId())) {
            logger.warn("getUserEvent: It's not user's event; eventId: {};", eventId);
            throw new ForbiddenException(getMessage("event.forbidden"));
        }
        return EventMapper.entityToDto(eventEntity, eventLanguageService.getLanguageListByEventId(eventId));
    }

    @Override
    public Page<EventDto> getUserEvents(Pageable pageable) {
        return eventRepository
                .getAllByUserId(accessChecker.getUserId(), pageable)
                .map(ent -> EventMapper.entityToDto(ent, eventLanguageService.getLanguageListByEventId(ent.getId())));
    }

    @Override
    public EventDto addEvent(EventDto eventDto) {
        EventEntity eventEntity = EventMapper.dtoToEntityForCreate(eventDto);
        eventEntity.setCategory(categoryService.getCategoryEntity(eventEntity.getCategory().getId()));
        eventEntity.setUserId(accessChecker.getUserId());
        eventEntity = eventRepository.save(eventEntity);
        eventLanguageService.addLanguageListByEventId(eventEntity.getId(), eventDto.getLanguages());
        logger.info("addEvent; event: {};", eventEntity);
        return EventMapper.entityToDto(eventEntity, eventLanguageService.getLanguageListByEventId(eventEntity.getId()));
    }

    @Override
    public EventDto updateEvent(Long eventId, EventDto eventDto) {
        EventEntity oldEntity = getEventEntity(eventId);
        if (!accessChecker.isUserId(oldEntity.getUserId())) {
            logger.warn("updateEvent: It's not user's event; eventId: {};", eventId);
            throw new ForbiddenException(getMessage("event.forbidden"));
        }
        EventEntity eventEntity = eventRepository.save(EventMapper.updateEntity(eventDto, oldEntity));
        eventEntity.setCategory(categoryService.getCategoryEntity(eventEntity.getCategory().getId()));
        eventLanguageService.addLanguageListByEventId(eventEntity.getId(), eventDto.getLanguages());
        logger.info("updateEvent; old: {}; new: {}", oldEntity, eventEntity);
        return EventMapper.entityToDto(eventEntity, eventLanguageService.getLanguageListByEventId(eventId));
    }

    @Override
    public void deleteEvent(Long eventId) {
        EventEntity eventEntity = getEventEntity(eventId);
        if (accessChecker.isUserId(eventEntity.getUserId())) {
            logger.warn("deleteUserEvent: It's not user's event; User-eventId: {}; eventId: {};", accessChecker.getUserId(), eventId);
            throw new ForbiddenException(getMessage("event.forbidden"));
        }
        eventEntity.setStatus(Status.DELETED);
        eventRepository.save(eventEntity);
    }

    @Override
    @Transactional
    public void setEndedStatusToAllExpired() {
        eventRepository.setStatusByEndDate(Status.ENDED, ZonedDateTime.now());
    }

    private EventEntity getEventEntity(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(getMessage("event.notFound")));
    }
}
