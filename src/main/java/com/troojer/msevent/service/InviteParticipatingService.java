package com.troojer.msevent.service;

import com.troojer.msevent.model.EventDto;

public interface InviteParticipatingService {
    boolean isInviteByPassword(String inviteKey);
    EventDto getEvent(String inviteKey, String invitePass);
    void acceptInvite(String inviteKey, String invitePass);
}
