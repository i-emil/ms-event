package com.troojer.msevent.service.impl;


import ch.qos.logback.classic.Logger;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.DatesMapper;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.OuterEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class OuterEventServiceImpl implements OuterEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final AccessCheckerUtil accessChecker;
    private final EventDataChecker eventDataChecker;
    private final ParticipantService participantService;

    @Override
    public EventDto getEvent(String eventKey) {
        EventEntity eventEntity = getEventEntity(eventKey);

        throwExceptionIfEventPrivateForUser(eventEntity);

        if (accessChecker.isUserId(eventEntity.getAuthorId()))
            return eventMapper.entityToDtoForAuthor(eventEntity);

        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        eventDataChecker.checkAllEventData(eventDto);
        EventEntity eventEntity = eventMapper.createEntity(eventDto, accessChecker.getUserId());
        eventRepository.save(eventEntity);
        logger.info("addEvent; event: {};", eventEntity);
        return eventMapper.entityToDtoForAuthor(eventEntity);
    }

    @Override
    public Page<EventDto> getEventsAsAuthor(StartEndDatesDto dates, Pageable pageable) {
        if (dates.isDisableDate())
            return eventRepository.getAllByAuthorId(accessChecker.getUserId(), pageable).map(eventMapper::simpleToDto);
        ZonedDateTime start = DatesMapper.dtoToEntity(dates.getStart());
        ZonedDateTime end = DatesMapper.dtoToEntity(dates.getEnd());
        return eventRepository.getAuthorEventsByDate(start, end, accessChecker.getUserId(), pageable).map(eventMapper::simpleToDto);
    }

    @Override
    public Page<EventDto> getEventsAsParticipant(StartEndDatesDto dates, Pageable pageable) {
        if (dates.isDisableDate())
            return eventRepository.getEventsPageByParticipant(accessChecker.getUserId(), List.of(ACTIVE), List.of(OK), pageable).map(eventMapper::simpleToDto);
        ZonedDateTime start = DatesMapper.dtoToEntity(dates.getStart());
        ZonedDateTime end = DatesMapper.dtoToEntity(dates.getEnd());
        return eventRepository.getEventsPageByParticipantAndDate(start, end, accessChecker.getUserId(), List.of(ACTIVE), List.of(OK), pageable).map(eventMapper::simpleToDto);
    }

    private EventEntity getEventEntity(String key) {
        return eventRepository.getFirstByKey(key)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
    }

    private void throwExceptionIfEventPrivateForUser(EventEntity eventEntity){
        if (eventEntity.isPrivate() && (accessChecker.isUserId(eventEntity.getAuthorId()) || participantService.getOkParticipants(eventEntity.getKey()).stream().anyMatch(p->accessChecker.isUserId(p.getProfile().getUserId()))))
            throw new ForbiddenException("event.event.forbidden");
    }
}
