package com.troojer.msevent.service;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantStatus;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface InnerEventService {

    List<EventEntity> getEventsByFilter(List<Long> eventsIdForCheck, FilterDto filter, ZonedDateTime before, ZonedDateTime after, List<EventStatus> eventStatuses, List<Long> eventsExceptList, Pageable pageable);

    Optional<EventEntity> getEventEntity(String key);

    EventEntity saveOrUpdateEntity(EventEntity eventEntity);

    List<SimpleEvent> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses);

    void setEndedStatusToAllExpired();
}
