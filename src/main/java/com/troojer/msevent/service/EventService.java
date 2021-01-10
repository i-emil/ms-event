package com.troojer.msevent.service;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventService {

    EventEntity getEventEntity(String key);

    EventDto getUserEvent(String key);

    Page<EventDto> getUserEvents(Pageable pageable);

    EventDto addEvent(EventDto eventDto);

    EventDto updateEvent(String key, EventDto eventDto);

    void deleteEvent(String key);

    List<EventEntity> getEventsByFilter(List<Long> eventsId, FilterDto filterDto, int days, List<Long> exceptEventId, Pageable pageable);

    EventEntity saveOrUpdateEntity(EventEntity eventEntity);

    List<EventEntity> getEventsByParticipant(String userId, List<EventStatus> statuses);

    void setEndedStatusToAllExpired();
}
