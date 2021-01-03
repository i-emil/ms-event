package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;

import java.time.LocalDateTime;

public interface FindEventService {
    EventDto getEvent(int days);

    void accept(String key);

    void reject(String key);

    void deleteOld(LocalDateTime beforeDateTime);

    void deleteInappropriate();

}
