package com.troojer.msevent.controller;

import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.service.ParticipantService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("private/events/participants")
public class PrivateParticipantController {

    private final ParticipantService participantService;

    public PrivateParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @DeleteMapping("inappropriate/{age}/{gender}")
    public void removeInappropriateEvents(@PathVariable Integer age, @PathVariable Gender gender) {
        participantService.leftInappropriateEvents(age, gender);
    }
}
