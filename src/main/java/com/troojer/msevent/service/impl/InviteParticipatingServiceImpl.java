package com.troojer.msevent.service.impl;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.InviteParticipatingService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;

@Service
public class InviteParticipatingServiceImpl implements InviteParticipatingService {

    private final EventMapper eventMapper;
    private final ParticipantService participantService;
    private final AccessCheckerUtil accessChecker;
    private final ProfileClient profileClient;
    private final InnerEventService innerEventService;

    public InviteParticipatingServiceImpl(EventMapper eventMapper, ParticipantService participantService, AccessCheckerUtil accessChecker, ProfileClient profileClient, InnerEventService innerEventService) {
        this.eventMapper = eventMapper;
        this.participantService = participantService;
        this.accessChecker = accessChecker;
        this.profileClient = profileClient;
        this.innerEventService = innerEventService;
    }

    @Override
    public boolean isInviteByPassword(String inviteKey) {
        EventEntity eventEntity = innerEventService.getEventByInviteKey(inviteKey, true).orElseThrow
                (() -> new NotFoundException("event.event.notFound"));
        return eventEntity.getInvitePassword() == null;
    }

    @Override
    public EventDto getEvent(String inviteKey, String invitePass) {
        EventEntity eventEntity = innerEventService.getEventByInviteKey(inviteKey, true).orElseThrow
                (() -> new NotFoundException("event.event.notFound"));
        String pass = eventEntity.getInvitePassword();
        if (pass != null && !pass.equals(invitePass)) throw new ForbiddenException("event.invite.wrongPassword");
        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    public void acceptInvite(String inviteKey, String invitePass) {
        EventEntity eventEntity = innerEventService.getEventByInviteKey(inviteKey, true).orElseThrow
                (() -> new NotFoundException("event.event.notFound"));
        String pass = eventEntity.getInvitePassword();
        if (pass != null && !pass.equals(invitePass)) throw new ForbiddenException("event.invite.wrongPassword");

        FilterDto filter = new FilterDto();
        filter.setProfileInfo(profileClient.getProfileFilter());
        List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(eventEntity.getId()), filter, ZonedDateTime.now(), ZonedDateTime.now().plusMonths(12), List.of(ACTIVE), List.of(), List.of(accessChecker.getUserId()), false, Pageable.unpaged());
        if (!checkEvent.isEmpty()) {
            participantService.joinEvent(eventEntity.getKey(), accessChecker.getUserId());
            return;
        }
        throw new ConflictException("event.accept.notAvailable");
    }
}
