package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;

public interface PrivateEventService {
    EventDto getPrivateEvent(String eventKey, String password);
    void acceptPrivateEvent(String eventKey, String password);
}
