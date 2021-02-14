package com.troojer.msevent.service.impl;


import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.mapper.StartEndDatesMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.OuterEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;
import static com.troojer.msevent.model.enm.ParticipantStatus.OK;


@Service
public class OuterEventServiceImpl implements OuterEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AccessCheckerUtil accessChecker;
    private final ImageClient imageClient;

    private final ParticipantService participantService;

    public OuterEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, AccessCheckerUtil accessChecker, ImageClient imageClient, ParticipantService participantService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.accessChecker = accessChecker;
        this.imageClient = imageClient;
        this.participantService = participantService;
    }

    @Override
    public EventDto getEvent(String key) {
        EventEntity eventEntity = getEventEntity(key);
        if (accessChecker.isUserId(eventEntity.getAuthorId()) || checkParticipating(key)) {
            return eventMapper.entityToDto(eventEntity);
        } else {
            logger.warn("getUserEvent: It's not user's event; eventId: {};", key);
            throw new ForbiddenException("event.event.forbidden");
        }
    }

    private boolean checkParticipating(String key) {
        return participantService.getParticipants(key, List.of(OK)).stream().anyMatch(x -> x.getProfile().getUserId().equals(accessChecker.getUserId()));
    }

    @Override
    @Transactional
    public Page<EventDto> getEvents(StartEndDatesDto dates, Pageable pageable) {
        ZonedDateTime start = StartEndDatesMapper.dtoToStartDate(dates);
        if (start.isAfter(ZonedDateTime.now())) start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = StartEndDatesMapper.dtoToEndDate(dates);
        if (end.isAfter(ZonedDateTime.now())) start = ZonedDateTime.now();
        return eventRepository.getAuthorEvents(start, end, accessChecker.getUserId(), pageable).map(eventMapper::simpleToDto);
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        EventEntity eventEntity = eventMapper.createEntity(eventDto, accessChecker.getUserId());
        eventRepository.save(eventEntity);
        logger.info("addEvent; event: {};", eventEntity);
        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    @Transactional
    public EventDto updateEvent(String key, EventDto eventDto) {
        EventEntity oldEntity = getEventEntity(key);
        if (!accessChecker.isUserId(oldEntity.getAuthorId())) {
            logger.warn("updateEvent: It's not user's event; eventId: {};", key);
            throw new ForbiddenException("event.event.forbidden");
        }
        EventEntity newEvent = eventMapper.updateEntity(eventDto, oldEntity);
        eventRepository.save(newEvent);

        logger.info("updateEvent; old: {}; new: {}", oldEntity, newEvent);
        return eventMapper.entityToDto(newEvent);
    }

    @Override
    public void deleteEvent(String key) {
        EventEntity eventEntity = getEventEntity(key);
        if (!accessChecker.isUserId(eventEntity.getAuthorId())) {
            logger.warn("deleteUserEvent: It's not user's event; User-eventId: {}; eventId: {};", accessChecker.getUserId(), key);
            throw new ForbiddenException("event.event.forbidden");
        }
        imageClient.deleteImage(eventEntity.getCover());
        eventEntity.setStatus(EventStatus.CANCELED);
        eventRepository.save(eventEntity);
        logger.info("deleteEvent(); eventId:{}", key);
    }

    @Override
    public List<EventDto> getEventsByParticipant(ZonedDateTime from, ZonedDateTime before) {
        return eventMapper.simpleEventsToDtos(eventRepository.getEventsByParticipant(from, before, accessChecker.getUserId(), List.of(ACTIVE), List.of(OK)));
    }

    private EventEntity getEventEntity(String key) {
        return eventRepository.getFirstByKey(key)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
    }
}
