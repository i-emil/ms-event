package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.RandomEventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("find-events")
@CrossOrigin
public class EventByFilterController {

    private final RandomEventService randomEventService;

    public EventByFilterController(RandomEventService randomEventService) {
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

    @PostMapping("accept/{key}")
    public void acceptEvent(@PathVariable String key) {
        randomEventService.accept(key);
    }

    @PostMapping("reject/{key}")
    public void rejectEvent(@PathVariable String key) {
        randomEventService.reject(key);
    }

    //todo private
    @DeleteMapping("inappropriate")
    public void removeInappropriateEvents(){
        randomEventService.deleteInappropriate();
    }
}
