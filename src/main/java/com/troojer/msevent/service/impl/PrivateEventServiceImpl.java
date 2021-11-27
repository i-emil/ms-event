package com.troojer.msevent.service.impl;

import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.ProfileInfo;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.service.PrivateEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventMapper eventMapper;
    private final ParticipantService participantService;
    private final AccessCheckerUtil accessChecker;
    private final ProfileClient profileClient;
    private final InnerEventService innerEventService;

    public PrivateEventServiceImpl(EventMapper eventMapper, ParticipantService participantService, AccessCheckerUtil accessChecker, ProfileClient profileClient, InnerEventService innerEventService) {
        this.eventMapper = eventMapper;
        this.participantService = participantService;
        this.accessChecker = accessChecker;
        this.profileClient = profileClient;
        this.innerEventService = innerEventService;
    }

    @Override
    public EventDto getPrivateEvent(String eventKey, String password) {
        EventEntity eventEntity = innerEventService.getEventEntity(eventKey).orElseThrow
                (() -> new NotFoundException("event.event.notFound"));
        String eventPassword = eventEntity.getPassword();
        if (eventPassword != null && !eventPassword.equals(password))
            throw new ForbiddenException("event.private.wrongPassword");
        return eventMapper.entityToDto(eventEntity);
    }

    @Override
    public void acceptPrivateEvent(String eventKey, String password) {
        EventEntity eventEntity = innerEventService.getEventEntity(eventKey).orElseThrow
                (() -> new NotFoundException("event.event.notFound"));
        String eventPassword = eventEntity.getPassword();
        if (eventPassword != null && !eventPassword.equals(password))
            throw new ForbiddenException("event.private.wrongPassword");

        Integer currentAge = null;
        Gender gender = null;

        if (!eventEntity.isFilterDisabled()) {
            ProfileInfo profileInfo = profileClient.getProfileFilter();
            currentAge = profileInfo.getCurrentAge();
            gender = profileInfo.getGender();
        }
        List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(eventEntity), null, currentAge, gender, List.of(), List.of(accessChecker.getUserId()), false, false);
        if (!checkEvent.isEmpty()) {
            participantService.joinEvent(eventEntity.getKey(), accessChecker.getUserId(), gender);
            return;
        }
        throw new ConflictException("event.accept.notAvailable");
    }
}
