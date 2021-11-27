package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventTagEntity;
import com.troojer.msevent.dao.repository.EventRepository;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.troojer.msevent.model.enm.ParticipantType.ALL;

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
    public List<EventEntity> getEventsByDateAndStatus(ZonedDateTime after, ZonedDateTime before, List<EventStatus> eventStatuses) {
        return eventRepository.getEventList(after, before, eventStatuses);
    }

    @Override
    public List<EventEntity> getEventsByFilter(List<EventEntity> eventList, Collection<Long> tagIdList, Integer currentAge, Gender gender, List<String> eventsKeyExceptList, List<String> usersExceptList, Boolean isEventPublic, Boolean isShuffledOrder) {
        ParticipantType participantType = (gender == null) ? null : ParticipantType.valueOf(gender.toString());

        List<EventEntity> filteredEventList = eventList.stream().filter(e ->
                (tagIdList == null || tagIdList.stream().anyMatch(tagId -> e.getTags().stream().map(EventTagEntity::getTagId).collect(Collectors.toList()).contains(tagId)))
                        && (e.getMinAge() == null || (currentAge >= e.getMinAge() && currentAge <= e.getMaxAge()))
                        && (e.getParticipantsType().isEmpty() || (e.getParticipantsType().containsKey(participantType) && e.getParticipantsType().get(participantType).isFree()) || (e.getParticipantsType().containsKey(ALL) && e.getParticipantsType().get(ALL).isFree()))
                        && (eventsKeyExceptList.isEmpty() || !eventsKeyExceptList.contains(e.getKey()))
                        && (usersExceptList.isEmpty() || !usersExceptList.contains(e.getAuthorId()))
                        && (!isEventPublic || e.getPassword() == null)
        ).collect(Collectors.toList());
        if (isShuffledOrder) Collections.shuffle(filteredEventList);
        logger.debug("getEventEntityByFilter(); eventEntityList: {}", filteredEventList);
        return filteredEventList;
    }

    @Override
    public Optional<EventEntity> getEventEntity(String key) {
        return eventRepository.getFirstByKey(key);
    }

    @Override
    @Transactional
    public void setEndedStatusToAllExpired() {
        eventRepository.setStatusByEndDate(EventStatus.ENDED, ZonedDateTime.now().minusDays(1), ZonedDateTime.now());
        logger.info("setEndedStatusToAllExpired(): done");
    }

    @Override
    public EventEntity saveOrUpdateEntity(EventEntity eventEntity) {
        return eventRepository.save(eventEntity);
    }

    @Override
    public List<EventEntity> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses) {
        return eventRepository.getParticipantEvents(after, before, userId, eventStatuses, participantStatuses);
    }

}