package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.SavedEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events/saved-events")
@RequiredArgsConstructor
public class SavedEventController {

    private final SavedEventService savedEventService;

    @GetMapping
    public Page<EventDto> getSavedEvents(Pageable pageable) {
        return savedEventService.getSavedEvents(pageable);
    }

    @PostMapping("{eventKey}")
    public void getSavedEvents(@PathVariable String eventKey) {
        savedEventService.saveEvent(eventKey);
    }

    @DeleteMapping("{eventKey}")
    public void deleteSavedEvent(@PathVariable String eventKey) {
        savedEventService.deleteSavedEvent(eventKey);
    }
}
