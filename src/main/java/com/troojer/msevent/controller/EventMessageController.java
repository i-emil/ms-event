package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventMessageDto;
import com.troojer.msevent.service.EventMessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("events/messages")
public class EventMessageController {

    private final EventMessageService eventMessageService;

    public EventMessageController(EventMessageService eventMessageService) {
        this.eventMessageService = eventMessageService;
    }

    @PostMapping("{eventKey}")
    public void addMessage(@PathVariable String eventKey,@RequestBody @Valid EventMessageDto message) {
        eventMessageService.addMessage(eventKey, message);
    }

    @GetMapping("{eventKey}")
    public Page<EventMessageDto> getMessages(@PathVariable String eventKey, Pageable pageable) {
        return eventMessageService.getMessages(eventKey, pageable);
    }
}
