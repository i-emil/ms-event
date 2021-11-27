package com.troojer.msevent.service;

import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.StartEndDatesDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;

public interface OuterEventService {

    EventDto getEvent(String eventKey);

    Page<EventDto> getEventsAsAuthor(StartEndDatesDto startEndDatesDto, Pageable pageable);

    EventDto createEvent(EventDto eventDto);

    Page<EventDto> getEventsAsParticipant(StartEndDatesDto dates, Pageable pageable);
}
