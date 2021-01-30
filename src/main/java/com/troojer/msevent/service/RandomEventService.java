package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface RandomEventService {
    EventDto getEvent(StartEndDatesDto dates);

    void accept(String key);

    void reject(String key);

    void deleteOld(LocalDateTime beforeDateTime);
}
