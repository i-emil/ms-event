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
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{id}")
    public EventDto getEvent(@PathVariable Long id) {
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
    public EventDto updateEvent(@PathVariable Long id, @RequestBody @Validated(UpdateValidation.class) EventDto eventDto) {
        return eventService.updateEvent(id, eventDto);
    }

    @DeleteMapping("{id}")
    public void deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
    }

}
