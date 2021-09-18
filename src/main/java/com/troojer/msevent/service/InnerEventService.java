package com.troojer.msevent.service;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantStatus;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface InnerEventService {

    List<EventEntity> getEventsByDateAndStatus(ZonedDateTime after, ZonedDateTime before, List<EventStatus> eventStatuses);

    List<EventEntity> getEventsByFilter(List<EventEntity> eventList, Long tagId, Integer currentAge, Gender gender, List<String> eventsKeyExceptList, List<String> authorsExceptList, Boolean isEventPublic, Boolean isShuffledOrder);

    Optional<EventEntity> getEventEntity(String key);

    EventEntity saveOrUpdateEntity(EventEntity eventEntity);

    List<EventEntity> getParticipantEvents(ZonedDateTime after, ZonedDateTime before, String userId, List<EventStatus> eventStatuses, List<ParticipantStatus> participantStatuses);

    void setEndedStatusToAllExpired();

    Optional<EventEntity> getEventByInviteKey(String inviteKey, boolean isInviteActive);
}
