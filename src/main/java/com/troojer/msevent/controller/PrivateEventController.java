package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.PrivateEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events/private")
@RequiredArgsConstructor
public class PrivateEventController {

    private final PrivateEventService privateEventService;

    @GetMapping("{eventKey}/{password}")
    public EventDto getEvent(@PathVariable String eventKey, String password) {
        return privateEventService.getPrivateEvent(eventKey, password);
    }

    @PostMapping("{inviteKey}/{password}")
    public void acceptPrivateEvent(@PathVariable String inviteKey, @PathVariable String password) {
        privateEventService.acceptPrivateEvent(inviteKey, password);
    }
}
