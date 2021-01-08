package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import com.troojer.msevent.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events")
@CrossOrigin
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{key}")
    public EventDto getEvent(@PathVariable String key) {
        return eventService.getUserEvent(key);
    }

    @GetMapping
    public Page<EventDto> getEvents(Pageable pageable) {
        return eventService.getUserEvents(pageable);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody @Validated(CreateValidation.class) EventDto eventDto) {
        return eventService.addEvent(eventDto);
    }

    @PutMapping("{key}")
    public EventDto updateEvent(@PathVariable String key, @RequestBody @Validated(UpdateValidation.class) EventDto eventDto) {
        return eventService.updateEvent(key, eventDto);
    }

    @DeleteMapping("{key}")
    public void deleteEvent(@PathVariable String key) {
        eventService.deleteEvent(key);
    }

}
