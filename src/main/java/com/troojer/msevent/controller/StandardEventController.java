package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.FilterValidation;
import com.troojer.msevent.service.OuterEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("events/standard")
public class StandardEventController {

    private final OuterEventService eventService;

    public StandardEventController(OuterEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{key}")
    public EventDto getEvent(@PathVariable String key) {
        return eventService.getEvent(key);
    }

    @PostMapping("filter")
    public Page<EventDto> getEvents(@RequestBody @Validated(FilterValidation.class) StartEndDatesDto startEndDatesDto, Pageable pageable) {
        return eventService.getEvents(startEndDatesDto, pageable);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody @Validated(CreateValidation.class) EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    //todo getAuthorEvents and participate event the same return type
    @PostMapping("participate")
    public List<EventDto> getEventsAsParticipant(@RequestBody @Validated(FilterValidation.class) StartEndDatesDto startEndDatesDto) {
        return eventService.getEventsByParticipant(startEndDatesDto);
    }

}
