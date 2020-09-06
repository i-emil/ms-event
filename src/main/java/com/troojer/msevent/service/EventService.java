package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EventService {

    EventDto getUserEvent(Long id);

    Page<EventDto> getUserEvents(Pageable pageable);

    EventDto addEvent(EventDto eventDto);

    EventDto updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

    void setEndedStatusToAllExpired();

}
