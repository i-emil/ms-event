package com.troojer.msevent.service;

import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface EventService {

    EventDto getUserEvent(String id);

    Page<EventDto> getUserEvents(Pageable pageable);

    EventDto addEvent(EventDto eventDto);

    EventDto updateEvent(String id, EventDto eventDto);

    void deleteEvent(String id);

    void setEndedStatusToAllExpired();

    EventEntity getEventByFilter(FilterDto filterDto, int days, List<String> exceptEventId);

    Optional<ParticipantType> raisePersonCountAndGetType(EventEntity eventEntity, Gender gender);
}
