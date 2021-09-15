package com.troojer.msevent.controller;

import com.troojer.msevent.service.InnerEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/events")
@RequiredArgsConstructor
public class PrivateExpiredEventsController {

    private final InnerEventService eventService;

    //fixedDelay = 5 * 60_000L
    @GetMapping("close-expired")
    public void closeOldEvents() {
        eventService.setEndedStatusToAllExpired();
    }

}
