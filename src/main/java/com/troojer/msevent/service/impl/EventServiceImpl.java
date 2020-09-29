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
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


@Service
public class EventServiceImpl implements EventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final CategoryService categoryService;

    private final AccessCheckerUtil accessChecker;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, CategoryService categoryService, AccessCheckerUtil accessChecker) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.categoryService = categoryService;
        this.accessChecker = accessChecker;
    }

    @Override
    @Transactional
    public EventDto getUserEvent(Long eventId) {
        EventEntity eventEntity = getEventEntity(eventId);
        if (!accessChecker.isUserId(eventEntity.getAuthorId())) {
            logger.warn("getUserEvent: It's not user's event; eventId: {};", eventId);
            throw new ForbiddenException("event.event.forbidden");
        }
        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    @Transactional
    public Page<EventDto> getUserEvents(Pageable pageable) {
        return eventRepository
                .getAllByAuthorId(accessChecker.getUserId(), pageable)
                .map(eventMapper::entityToDto);
    }

    @Override
    @Transactional
    public EventDto addEvent(EventDto eventDto) {
        EventEntity eventEntity = eventMapper.createEntity(eventDto);

        eventEntity.setCategory(categoryService.getCategoryEntity(eventEntity.getCategory().getId()));
        eventEntity.getLanguages().forEach(lang -> lang.setEvent(eventEntity));
        eventEntity.getTags().forEach(tag -> tag.setEvent(eventEntity));
        eventEntity.setAuthorId(accessChecker.getUserId());

        eventRepository.save(eventEntity);
        logger.info("addEvent; event: {};", eventEntity);
        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long eventId, EventDto eventDto) {
        EventEntity oldEntity = getEventEntity(eventId);
        if (!accessChecker.isUserId(oldEntity.getAuthorId())) {
            logger.warn("updateEvent: It's not user's event; eventId: {};", eventId);
            throw new ForbiddenException("event.event.forbidden");
        }
        EventEntity newEvent = eventMapper.updateEntity(eventDto, oldEntity);
        newEvent.getLanguages().forEach(lang -> lang.setEvent(newEvent));
        newEvent.getTags().forEach(tag -> tag.setEvent(newEvent));
        newEvent.setCategory(categoryService.getCategoryEntity(newEvent.getCategory().getId()));
        eventRepository.save(newEvent);

        logger.info("updateEvent; old: {}; new: {}", oldEntity, newEvent);
        return eventMapper.entityToDto(newEvent);
    }

    @Override
    public void deleteEvent(Long eventId) {
        EventEntity eventEntity = getEventEntity(eventId);
        if (accessChecker.isUserId(eventEntity.getAuthorId())) {
            logger.warn("deleteUserEvent: It's not user's event; User-eventId: {}; eventId: {};", accessChecker.getUserId(), eventId);
            throw new ForbiddenException("event.event.forbidden");
        }
        eventEntity.setStatus(Status.DELETED);
        eventRepository.save(eventEntity);
        logger.info("deleteEvent(); eventId:{}", eventId);
    }

    @Override
    @Transactional
    public void setEndedStatusToAllExpired() {
        eventRepository.setStatusByEndDate(Status.ENDED, ZonedDateTime.now());
        logger.info("setEndedStatusToAllExpired(): done");
    }

    private EventEntity getEventEntity(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
    }
}
