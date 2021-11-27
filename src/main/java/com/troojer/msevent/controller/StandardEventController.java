package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.StartEndDatesDto;
import com.troojer.msevent.model.label.CreateValidation;
import com.troojer.msevent.model.label.FilterValidation;
import com.troojer.msevent.service.OuterEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events/standard")
@RequiredArgsConstructor
public class StandardEventController {

    private final OuterEventService eventService;

    @GetMapping("{key}")
    public EventDto getEvent(@PathVariable String key) {
        return eventService.getEvent(key);
    }

    @PostMapping
    public EventDto addEvent(@RequestBody @Validated(CreateValidation.class) EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @PostMapping("filter-author")
    public Page<EventDto> getEventsAsAuthor(@RequestBody @Validated(FilterValidation.class) StartEndDatesDto startEndDatesDto, Pageable pageable) {
        return eventService.getEventsAsAuthor(startEndDatesDto, pageable);
    }

    @PostMapping("filter-participant")
    public Page<EventDto> getEventsAsParticipant(@RequestBody @Validated(FilterValidation.class) StartEndDatesDto startEndDatesDto, Pageable pageable) {
        return eventService.getEventsAsParticipant(startEndDatesDto, pageable);
    }
}
