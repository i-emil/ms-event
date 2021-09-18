package com.troojer.msevent.controller;

import com.troojer.msevent.service.ParticipantService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("private/events/participants")
public class PrivateParticipantController {

    private final ParticipantService participantService;

    public PrivateParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @DeleteMapping("inappropriate/{age}/{gender}")
    public void removeInappropriateEvents(@PathVariable String age, @PathVariable String gender) {
        participantService.leftInappropriateEvents();
    }
}
