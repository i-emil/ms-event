package com.troojer.msevent.controller;

import com.troojer.msevent.service.ParticipantService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/event-participants")
@CrossOrigin
public class PrivateParticipantController {

    private final ParticipantService participantService;

    public PrivateParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @DeleteMapping("inappropriate")
    public void removeInappropriateEvents() {
        participantService.leftInappropriateEvents();
    }
}
