package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.UpdateValidation;
import com.troojer.msevent.service.OuterEventService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.MediaSize;
import java.time.ZonedDateTime;

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

@RestController
@RequestMapping("events/standard")
@CrossOrigin
public class StandardEventController {

    private final OuterEventService eventService;

    public StandardEventController(OuterEventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("{key}")
    public EventDto getEvent(@PathVariable String key) {
        return eventService.getEvent(key);
    }

    //todo check date
    @GetMapping
    public Page<EventDto> getEvents(@RequestParam @DateTimeFormat(iso = DATE_TIME) ZonedDateTime after, @RequestParam @DateTimeFormat(iso = DATE_TIME) ZonedDateTime before, Pageable pageable) {
        return eventService.getEvents(after, before, pageable);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody @Validated(CreateValidation.class) EventDto eventDto) {
        return eventService.createEvent(eventDto);
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
