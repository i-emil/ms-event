package com.troojer.msevent.service.impl;


import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.mapper.LanguageMapper;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.troojer.msevent.model.enm.ParticipantType.ALL;


@Service
public class EventServiceImpl implements EventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AccessCheckerUtil accessChecker;
    private final ImageClient imageClient;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, AccessCheckerUtil accessChecker, ImageClient imageClient) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.accessChecker = accessChecker;
        this.imageClient = imageClient;
    }

    @Override
    @Transactional
    public EventDto getUserEvent(String key) {
        EventEntity eventEntity = getEventEntity(key);
        if (!accessChecker.isUserId(eventEntity.getAuthorId())) {
            logger.warn("getUserEvent: It's not user's event; eventId: {};", key);
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
        eventEntity.setStatus(EventStatus.DELETED);
        eventRepository.save(eventEntity);
        logger.info("deleteEvent(); eventId:{}", key);
    }

    @Override
    @Transactional
    public void setEndedStatusToAllExpired() {
        eventRepository.setStatusByEndDate(EventStatus.ENDED, ZonedDateTime.now());
        logger.info("setEndedStatusToAllExpired(): done");
    }

    @Override
    public List<EventEntity> getEventsByFilter(List<Long> eventsIdForCheck, FilterDto filterDto, int days, List<Long> eventsExceptList, Pageable pageable) {
        if (eventsExceptList.isEmpty()) eventsExceptList = List.of(-1L);
        if (eventsIdForCheck.isEmpty()) eventsIdForCheck = List.of(-1L);
        String userId = accessChecker.getUserId();
        AgeDto ageDto = filterDto.getAge();
        List<EventEntity> eventEntityList = eventRepository.getListByFilter(eventsIdForCheck, ZonedDateTime.now().plusHours(1), ZonedDateTime.now().plusDays(days),
                ageDto.getMin(), ageDto.getMax(), ageDto.getCurrent(),
                LanguageMapper.dtosToIds(filterDto.getLanguages()), eventsExceptList, userId, pageable);
        logger.info("getEventEntityByFilter(); eventEntityList: {}", eventEntityList);
        System.out.println("qwerty: " + eventEntityList);
        return eventEntityList;
    }

    private EventEntity getEventEntity(String key) {
        return eventRepository.getFirstByKey(key)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
    }

    @Override
    public Optional<ParticipantType> raisePersonCountAndGetType(EventEntity event, Gender userGender) {
        Map<ParticipantType, EventParticipantTypeEntity> eventParticipantsType = event.getParticipantsType();
        ParticipantType participantType = null;
        if (eventParticipantsType.get(ParticipantType.valueOf(userGender.toString())).increaseAccepted())
            participantType = ParticipantType.valueOf(userGender.toString());
        else if (eventParticipantsType.get(ALL).increaseAccepted()) participantType = ALL;
        eventRepository.save(event);
        return participantType != null ? Optional.of(participantType) : Optional.empty();
    }

}
