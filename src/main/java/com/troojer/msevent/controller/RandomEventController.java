package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.model.label.FilterValidation;
import com.troojer.msevent.service.RandomEventService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events/random")
@CrossOrigin
public class RandomEventController {

    private final RandomEventService randomEventService;

    public RandomEventController(RandomEventService randomEventService) {
        this.randomEventService = randomEventService;
    }

    @PostMapping("standard")
    public EventDto getEventForDay(@RequestBody @Validated(FilterValidation.class) StartEndDatesDto startEndDatesDto) {
        return randomEventService.getEvent(startEndDatesDto);
    }

    @PostMapping("accept/{key}")
    public void acceptEvent(@PathVariable String key) {
        randomEventService.accept(key);
    }

    @PostMapping("reject/{key}")
    public void rejectEvent(@PathVariable String key) {
        randomEventService.reject(key);
    }

}
