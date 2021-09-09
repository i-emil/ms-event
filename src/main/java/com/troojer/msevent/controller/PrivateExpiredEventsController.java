package com.troojer.msevent.controller;

import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.RandomEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("private/events")
@RequiredArgsConstructor
public class PrivateExpiredEventsController {

    private final InnerEventService eventService;
    private final RandomEventService randomEventService;

    //fixedDelay = 5 * 60_000L
    @GetMapping("close-expired")
    public void closeOldEvents() {
        eventService.setEndedStatusToAllExpired();
    }

    //todo skoree vsego logika budet izmenena
    //fixedDelay = 30 * 24 * 60 * 60_000L
    @DeleteMapping("old-user-found-events")
    public void deleteOldUserFoundEvents() {
        randomEventService.deleteOld(LocalDateTime.now().minusMonths(24));
    }
}
