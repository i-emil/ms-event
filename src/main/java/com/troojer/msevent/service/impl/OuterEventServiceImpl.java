package com.troojer.msevent.service.impl;


import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.client.LocationClient;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.client.UserPlanClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.mapper.StartEndDatesMapper;
import com.troojer.msevent.model.*;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.InvalidEntityException;
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
import java.util.Map;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;
import static com.troojer.msevent.model.enm.ParticipantStatus.OK;


@Service
public class OuterEventServiceImpl implements OuterEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AccessCheckerUtil accessChecker;

    private final LocationClient locationClient;
    private final ImageClient imageClient;
    private final ProfileClient profileClient;
    private final UserPlanClient userPlanClient;

    private final ParticipantService participantService;

    public OuterEventServiceImpl(EventRepository eventRepository, EventMapper eventMapper, AccessCheckerUtil accessChecker, LocationClient locationClient, ImageClient imageClient, ProfileClient profileClient, UserPlanClient userPlanClient, ParticipantService participantService) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.accessChecker = accessChecker;
        this.locationClient = locationClient;
        this.imageClient = imageClient;
        this.profileClient = profileClient;
        this.userPlanClient = userPlanClient;
        this.participantService = participantService;
    }

    @Override
    public EventDto getEvent(String key) {
        EventEntity eventEntity = getEventEntity(key);
        if (accessChecker.isUserId(eventEntity.getAuthorId())) {
            return eventMapper.entityToDtoForAuthor(eventEntity);
        } else if (participantService.checkParticipating(key)) {
            return eventMapper.entityToDto(eventEntity);
        } else {
            logger.warn("getUserEvent: It's not user's event; eventId: {};", key);
            throw new ForbiddenException("event.event.forbidden");
        }
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
        checkLocationAndCover(eventDto);
        checkParticipantCount(eventDto.getParticipantsType());
        checkAge(eventDto);
        EventEntity eventEntity = eventMapper.createEntity(eventDto, accessChecker.getUserId());
        eventRepository.save(eventEntity);
        participantService.joinEvent(eventEntity.getKey(), accessChecker.getUserId());
        logger.info("addEvent; event: {};", eventEntity);
        return eventMapper.entityToDtoForAuthor(eventEntity);
    }

    @Override
    public List<EventDto> getEventsByParticipant(StartEndDatesDto dates) {
        ZonedDateTime start = StartEndDatesMapper.dtoToStartDate(dates);
        if (start.isAfter(ZonedDateTime.now())) start = ZonedDateTime.now().minusMinutes(5);
        ZonedDateTime end = StartEndDatesMapper.dtoToEndDate(dates);
        return eventMapper.simpleEventsToDtos(eventRepository.getEventsByParticipant(start, end, accessChecker.getUserId(), List.of(ACTIVE), List.of(OK)));
    }

    private EventEntity getEventEntity(String key) {
        return eventRepository.getFirstByKey(key)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
    }

    private void checkLocationAndCover(EventDto eventDto) {
        try {
            locationClient.getLocation(eventDto.getLocation().getId());
            imageClient.isImageExist(eventDto.getCover());
        } catch (NotFoundException e) {
            throw new InvalidEntityException(e.getMessage());
        }
    }

    private void checkParticipantCount(Map<ParticipantType, EventParticipantTypeDto> participantsType) {
        int maxPersonCount = userPlanClient.getPermitValue(accessChecker.getPlan(), "EVENT_PERSON_MAX_COUNT");
        int minPersonCount = userPlanClient.getPermitValue(accessChecker.getPlan(), "EVENT_PERSON_MIN_COUNT");

        int maleCount = (participantsType.get(ParticipantType.MALE) == null) ? 0 : participantsType.get(ParticipantType.MALE).getTotal();
        int femaleCount = (participantsType.get(ParticipantType.FEMALE) == null) ? 0 : participantsType.get(ParticipantType.FEMALE).getTotal();
        int allCount = (participantsType.get(ParticipantType.ALL) == null) ? 0 : participantsType.get(ParticipantType.ALL).getTotal();

        int totalPersonCount = maleCount + femaleCount + allCount;

        Gender gender = Gender.valueOf(profileClient.getProfileFilter().getGender());
        if (((gender == Gender.MALE && maleCount > 0) || (gender == Gender.FEMALE && femaleCount > 0) || allCount > 0)
                && totalPersonCount >= minPersonCount && totalPersonCount <= maxPersonCount) return;
        throw new InvalidEntityException("person count is incorrect");
    }

    private void checkAge(EventDto eventDto) {
        AgeDto ageDto = eventDto.getAge();
        Integer minAge = ageDto.getMin();
        Integer maxAge = ageDto.getMax();
        int min = 18;
        int max = 100;
        Integer current = profileClient.getProfileFilter().getAge().getCurrent();
        if (minAge != null && maxAge != null &&
                minAge <= maxAge &&
                minAge >= min && maxAge <= max &&
                current >= minAge && current <= maxAge) return;
        throw new InvalidEntityException("age range is: " + min + "-" + max + " and userAge have to be in selected range");
    }
}
