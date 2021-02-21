package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.mapper.LanguageMapper;
import com.troojer.msevent.model.AgeDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InnerEventServiceImpl implements InnerEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventRepository eventRepository;
    private final AccessCheckerUtil accessChecker;

    public InnerEventServiceImpl(EventRepository eventRepository, AccessCheckerUtil accessChecker, ImageClient imageClient) {
        this.eventRepository = eventRepository;
        this.accessChecker = accessChecker;
    }

    @Override
    public Optional<EventEntity> getEventEntity(String key) {
        return eventRepository.getFirstByKey(key);
    }

    @Override
    @Transactional
    public void setEndedStatusToAllExpired() {
        eventRepository.setStatusByEndDate(EventStatus.ENDED,ZonedDateTime.now().minusDays(1), ZonedDateTime.now());
        logger.info("setEndedStatusToAllExpired(): done");
    }

    @Override
    public Optional<EventEntity> getEventByInviteKey(String inviteKey, boolean isInviteActive) {
        return eventRepository.getFirstByInviteKeyAndInviteActive(inviteKey, isInviteActive);
    }

    @Override
    public List<EventEntity> getEventsByFilter(List<Long> eventsIdForCheck, FilterDto filter, ZonedDateTime after, ZonedDateTime before, List<EventStatus> eventStatuses, List<Long> eventsExceptList, List<String> authorsExceptList, Boolean isEventPublic, Pageable pageable) {
        if (eventsIdForCheck.isEmpty()) eventsIdForCheck = List.of(-1L);
        if (eventsExceptList.isEmpty()) eventsExceptList = List.of(-1L);
        if (authorsExceptList.isEmpty()) authorsExceptList = List.of(" ");

        AgeDto ageDto = filter.getAge();
        ParticipantType participantType = ParticipantType.valueOf(filter.getGender());
        List<EventEntity> eventEntityList = eventRepository.getListByFilter(eventsIdForCheck, after, before, eventStatuses,
                ageDto.getMin(), ageDto.getMax(), ageDto.getCurrent(), participantType,
                LanguageMapper.dtosToIds(filter.getLanguages()), eventsExceptList, authorsExceptList, isEventPublic, pageable);
        logger.debug("getEventEntityByFilter(); eventEntityList: {}", eventEntityList);
        return eventEntityList;
    }

    @Override
    public EventEntity saveOrUpdateEntity(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

    @Override
    public List<SimpleEvent> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses) {
        return eventRepository.getEventsByParticipant(after, before, userId, eventStatuses, participantStatuses);
    }

}