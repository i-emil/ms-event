package com.troojer.msevent.controller;

import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.troojer.msevent.model.enm.ParticipantStatus.LEFT;

@RestController
@RequestMapping("event-participants")
public class ParticipantController {

    private final ParticipantService participantService;
    private final AccessCheckerUtil accessChecker;

    public ParticipantController(ParticipantService participantService, AccessCheckerUtil accessChecker) {
        this.participantService = participantService;
        this.accessChecker = accessChecker;
    }

    @DeleteMapping("{eventKey}")
    public void leftEvent(@PathVariable String eventKey) {
        participantService.leftEvent(eventKey);
    }

}
