package com.troojer.msevent.controller;

import com.troojer.msevent.service.ParticipantService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("events/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @DeleteMapping("{eventKey}")
    public void leftEvent(@PathVariable String eventKey) {
        participantService.deleteFromEvent(eventKey);
    }

}
