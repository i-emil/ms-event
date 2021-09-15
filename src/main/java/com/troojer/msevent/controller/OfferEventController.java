package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.service.OfferEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("events/offer")
@RequiredArgsConstructor
public class OfferEventController {

    private final OfferEventService offerEventService;

    @PostMapping("filter")
    public List<EventDto> getEventsByFilter(@RequestBody @Valid FilterDto filter) {
        return offerEventService.getOfferEvents(filter);
    }

    @PostMapping("accept/{eventKey}")
    public void acceptEvent(@PathVariable String eventKey) {
        offerEventService.accept(eventKey);
    }


}
