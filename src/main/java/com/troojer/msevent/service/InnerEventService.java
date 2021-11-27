package com.troojer.msevent.service;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantStatus;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface InnerEventService {

    List<EventEntity> getEventsByDateAndStatus(ZonedDateTime after, ZonedDateTime before, List<EventStatus> eventStatuses);

    List<EventEntity> getEventsByFilter(List<EventEntity> eventList, Collection<Long> tagIdList, Integer currentAge, Gender gender, List<String> eventsKeyExceptList, List<String> usersExceptList, Boolean isEventPublic, Boolean isShuffledOrder);

    Optional<EventEntity> getEventEntity(String key);

    EventEntity saveOrUpdateEntity(EventEntity eventEntity);

    List<EventEntity> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses);

    void setEndedStatusToAllExpired();
}
