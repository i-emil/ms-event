package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.service.FindEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events")
public class EventController {

    private final EventService eventService;
    private final FindEventService findEventService;

    public EventController(EventService eventService, FindEventService findEventService) {
        this.eventService = eventService;
        this.findEventService = findEventService;
    }

    @GetMapping("{id}")
    public EventDto getEvent(@PathVariable String id) {
        return eventService.getUserEvent(id);
    }

    @GetMapping
    public Page<EventDto> getEvents(Pageable pageable) {
        return eventService.getUserEvents(pageable);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody @Validated(CreateValidation.class) EventDto eventDto) {
        return eventService.addEvent(eventDto);
    }

    @PutMapping("{id}")
    public EventDto updateEvent(@PathVariable String id, @RequestBody @Validated(UpdateValidation.class) EventDto eventDto) {
        return eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable String id) {
        eventService.deleteEvent(id);
    }

}
