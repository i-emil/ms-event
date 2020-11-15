package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.FindEventService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("find-events")
public class EventByFilterController {

    private final FindEventService findEventService;

    public EventByFilterController(FindEventService findEventService) {
        this.findEventService = findEventService;
    }

    @GetMapping("day")
    public EventDto getEventForDay() {
        return findEventService.getEvent(1);
    }

    @GetMapping("week")
    public EventDto getEventForWeek() {
        return findEventService.getEvent(7);
    }

    @PostMapping("accept/{key}")
    public void acceptEvent(@PathVariable String key) {
        findEventService.accept(key);
    }

    @PostMapping("reject/{key}")
    public void rejectEvent(@PathVariable String key) {
        findEventService.reject(key);
    }

}
