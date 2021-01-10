package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.RandomEventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("random-events")
@CrossOrigin
public class RandomEventController {

    private final RandomEventService randomEventService;

    public RandomEventController(RandomEventService randomEventService) {
        this.randomEventService = randomEventService;
    }

    @GetMapping("day")
    public EventDto getEventForDay() {
        return randomEventService.getEvent(1);
    }

    @GetMapping("week")
    public EventDto getEventFor7Days() {
        return randomEventService.getEvent(7);
    }

    @PostMapping("accept/{key}/{isCouple}")
    public void acceptEvent(@PathVariable String key, @PathVariable boolean isCouple) {
        randomEventService.accept(key, isCouple);
    }

    @PostMapping("reject/{key}")
    public void rejectEvent(@PathVariable String key) {
        randomEventService.reject(key);
    }

}
