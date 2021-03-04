package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.dao.SimpleEvent;
import com.troojer.msevent.dao.repository.ParticipantRepository;
import com.troojer.msevent.mapper.ParticipantMapper;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;
import static com.troojer.msevent.model.enm.ParticipantStatus.*;
import static com.troojer.msevent.model.enm.ParticipantType.ALL;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final ParticipantRepository participantRepository;
    private final InnerEventService innerEventService;
    private final ParticipantMapper participantMapper;
    private final ProfileClient profileClient;
    private final AccessCheckerUtil accessChecker;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, InnerEventService innerEventService, ParticipantMapper participantMapper, ProfileClient profileClient, AccessCheckerUtil accessChecker) {
        this.participantRepository = participantRepository;
        this.innerEventService = innerEventService;
        this.participantMapper = participantMapper;
        this.profileClient = profileClient;
        this.accessChecker = accessChecker;
    }

    public List<ParticipantDto> getOkParticipants(String eventKey) {
        if (checkParticipating(eventKey))
            return getParticipants(eventKey, List.of(OK));
        logger.warn("getOkParticipants: It's not user's event; eventId: {};", eventKey);
        throw new ForbiddenException("event.event.forbidden");
    }

    @Override
    public List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> status) {
        return participantMapper.entitiesToDtos(participantRepository.getParticipants(eventKey, status));
    }

    public void joinEvent(String eventKey, String userId) {
        Optional<EventEntity> eventOpt = innerEventService.getEventEntity(eventKey);
        if (eventOpt.isPresent()) {
            EventEntity event = eventOpt.get();
            try {
                Optional<ParticipantType> participantTypeOpt = getParticipantTypeByProfile(event);
                if (event.getStatus() == EventStatus.ACTIVE && participantTypeOpt.isPresent()) {
                    Optional<EventParticipantTypeEntity> eventParticipantTypeOpt = getEventParticipantEntityByUserParticipantType(eventKey, participantTypeOpt.get());
                    if (eventParticipantTypeOpt.isPresent()) {
                        EventParticipantTypeEntity eventParticipantType = eventParticipantTypeOpt.get();
                        addParticipant(eventParticipantType.getId(), ParticipantEntity.builder().userId(userId).event(event).type(eventParticipantType.getType()).build());
                        return;
                    }
                }
            } catch (Exception e) {
                logger.warn("joinEvent(); something wrong; eventKey: {}; userId: {}", eventKey, userId);
            }
        }
        throw new ConflictException("event.accept.notAvailable");
    }

    @Override
    public void deleteFromEvent(String eventKey) {
        if (!deleteFromEvent(eventKey, accessChecker.getUserId(), LEFT))
            throw new NotFoundException("participant.left.notFound");
    }

    @Override
    @Transactional
    public boolean deleteFromEvent(String eventKey, String userId, ParticipantStatus reason) {
        if (reason == DELETED_BY_AUTHOR || reason == LEFT || reason == INAPPROPRIATE) {
            try {
                Optional<EventEntity> eventOpt = innerEventService.getEventEntity(eventKey);
                if (eventOpt.isPresent()) {
                    EventEntity event = eventOpt.get();
                    Optional<ParticipantEntity> participantOpt = participantRepository.getFirstByEventIdAndUserIdAndStatusIn(event.getId(), userId, List.of(OK));
                    if (participantOpt.isPresent()) {
                        ParticipantEntity participant = participantOpt.get();
                        participant.setStatus(reason);
                        event.getParticipantsType().get(participant.getType()).decreaseAccepted();
                        participantRepository.save(participant);
                        innerEventService.saveOrUpdateEntity(event);
                        return true;
                    }
                }
            } catch (Exception e) {
                logger.warn("deleteParticipant(); exc: ", e);
            }
            return false;
        }
        throw new IllegalArgumentException("deleting reason is illegal");
    }

    @Override
    public void leftInappropriateEvents() {
        List<SimpleEvent> oldUserEvents = innerEventService.getParticipantEvents(ZonedDateTime.now().minusYears(1), ZonedDateTime.now(), accessChecker.getUserId(), List.of(ACTIVE), List.of(OK));
        if (oldUserEvents.isEmpty()) return;
        List<Long> userEventsId = oldUserEvents.stream().map(SimpleEvent::getId).collect(Collectors.toList());
        FilterDto filter = profileClient.getProfileFilter();
        List<Long> currentUserEvents = innerEventService.getEventsByFilter(userEventsId, filter, ZonedDateTime.now().plusMinutes(10), ZonedDateTime.now().plusMonths(6), List.of(ACTIVE), List.of(), List.of(), false, Pageable.unpaged()).stream().map(EventEntity::getId).collect(Collectors.toList());
        List<String> inappropriate = oldUserEvents.stream().filter(e -> !currentUserEvents.contains(e.getId()) && e.getStatus() == ACTIVE).map(SimpleEvent::getKey).collect(Collectors.toList());
        inappropriate.forEach(eventKey -> deleteFromEvent(eventKey, accessChecker.getUserId(), ParticipantStatus.INAPPROPRIATE));
    }

    private Optional<EventParticipantTypeEntity> getEventParticipantEntityByUserParticipantType(String eventKey, ParticipantType userType) {
        try {
            Optional<EventEntity> eventOpt = innerEventService.getEventEntity(eventKey);
            if (eventOpt.isPresent()) {
                EventEntity event = eventOpt.get();
                Map<ParticipantType, EventParticipantTypeEntity> participantTypeMap = event.getParticipantsType();
                if (participantTypeMap.containsKey(userType) && participantTypeMap.get(userType).isFree())
                    return Optional.of(participantTypeMap.get(userType));
                if (participantTypeMap.containsKey(ALL) && participantTypeMap.get(ALL).isFree())
                    return Optional.of(participantTypeMap.get(ALL));
            }
        } catch (Exception e) {
            logger.warn("getEventParticipantEntityByUserParticipantType(); exc: ", e);
        }
        return Optional.empty();
    }

    private void addParticipant(Long participantTypeId, ParticipantEntity participant) {
        participantRepository.addParticipant(participantTypeId, participant);
    }

    private Optional<ParticipantType> getParticipantTypeByProfile(EventEntity event) {
        FilterDto filter = profileClient.getProfileFilter();
        return Optional.of(ParticipantType.valueOf(filter.getGender()));
    }

    public boolean checkParticipating(String key) {
        return getParticipants(key, List.of(OK)).stream().anyMatch(x -> x.getProfile().getUserId().equals(accessChecker.getUserId()));
    }
}
