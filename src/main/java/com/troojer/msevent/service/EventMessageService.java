package com.troojer.msevent.service;

import com.troojer.msevent.model.EventMessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EventMessageService {

    void addMessage(String eventKey, EventMessageDto eventMessageDto);

    Page<EventMessageDto> getMessages(String eventKey, Pageable pageable);

}
