package com.troojer.msevent.controller;

import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.service.InviteParticipatingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("events/inviting")
public class InvitingController {
    private final InviteParticipatingService inviteParticipatingService;

    public InvitingController(InviteParticipatingService inviteParticipatingService) {
        this.inviteParticipatingService = inviteParticipatingService;
    }

    @GetMapping("is-password-required/{inviteKey}")
    public boolean getEvent(@PathVariable String inviteKey){
        return inviteParticipatingService.isInviteByPassword(inviteKey);
    }

    @GetMapping("{inviteKey}/{invitePassword}")
    public EventDto getEvent(@PathVariable String inviteKey, @PathVariable String invitePassword){
        return inviteParticipatingService.getEvent(inviteKey, invitePassword);
    }

    @PostMapping("{inviteKey}/{invitePassword}")
    public void accept(@PathVariable String inviteKey, @PathVariable String invitePassword){
        inviteParticipatingService.acceptInvite(inviteKey, invitePassword);
    }
}
