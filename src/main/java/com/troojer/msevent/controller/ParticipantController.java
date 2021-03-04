package com.troojer.msevent.controller;

import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.service.ParticipantService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("events/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("{eventKey}")
    public List<ParticipantDto> getParticipants(@PathVariable String eventKey) {
        return participantService.getOkParticipants(eventKey);
    }

    @DeleteMapping("{eventKey}")
    public void leftEvent(@PathVariable String eventKey) {
        participantService.deleteFromEvent(eventKey);
    }

}
