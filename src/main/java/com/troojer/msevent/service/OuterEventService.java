package com.troojer.msevent.service;

import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface OuterEventService {

    EventDto getEvent(String key);

    Page<EventDto> getEvents(ZonedDateTime from, ZonedDateTime before, Pageable pageable);

    EventDto createEvent(EventDto eventDto);

    EventDto updateEvent(String key, EventDto eventDto);

    void deleteEvent(String key);

    List<EventDto> getEventsByParticipant(ZonedDateTime from, ZonedDateTime before);

}
