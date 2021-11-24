package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.dao.repository.ParticipantRepository;
import com.troojer.msevent.mapper.ParticipantMapper;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
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
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final ParticipantRepository participantRepository;
    private final InnerEventService innerEventService;
    private final ParticipantMapper participantMapper;
    private final AccessCheckerUtil accessChecker;

    public List<ParticipantDto> getOkParticipants(String eventKey) {
        Optional<EventEntity> eventOpt = innerEventService.getEventEntity(eventKey);
        if (eventOpt.isPresent() && !eventOpt.get().isPrivate())
            return getParticipants(eventKey, List.of(OK));
        logger.warn("getOkParticipants: It's not user's event; eventId: {};", eventKey);
        throw new ForbiddenException("event.event.forbidden");
    }

    @Override
    public List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> statuses) {
        return participantMapper.entitiesToDtos(participantRepository.getParticipants(eventKey, statuses));
    }

    public void joinEvent(String eventKey, String userId, Gender gender) {
        Optional<EventEntity> eventOpt = innerEventService.getEventEntity(eventKey);
        if (eventOpt.isPresent()) {
            EventEntity event = eventOpt.get();
            try {
                if (event.getStatus() == EventStatus.ACTIVE) {
                    if (!event.isLimitlessParticipating()) {
                        participantRepository.save(ParticipantEntity.builder().userId(userId).event(event).type(ALL).build());
                        return;
                    }
                    ParticipantType participantTypeByGender = ParticipantType.valueOf(gender.toString());
                    Optional<EventParticipantTypeEntity> eventParticipantTypeOpt = getEventParticipantEntityByUserParticipantType(eventKey, participantTypeByGender);
                    if (eventParticipantTypeOpt.isPresent()) {
                        EventParticipantTypeEntity eventParticipantType = eventParticipantTypeOpt.get();
                        addParticipant(eventParticipantType.getId(), ParticipantEntity.builder().userId(userId).event(event).type(eventParticipantType.getType()).build());
                        return;
                    }
                }
            } catch (Exception e) {
                logger.warn("joinEvent(); something wrong; eventKey: {}, exc: ", eventKey, e);
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
    public void leftInappropriateEvents(Integer age, Gender gender) {
        List<EventEntity> userEvents = innerEventService.getParticipantEvents(ZonedDateTime.now().minusYears(1), ZonedDateTime.now(), accessChecker.getUserId(), List.of(ACTIVE), List.of(OK)).stream().filter(e -> !e.isFilterDisabled()).collect(Collectors.toList());
        if (userEvents.isEmpty()) return;
        List<EventEntity> inappropriateEvents = innerEventService.getEventsByFilter(userEvents, null, age, gender, List.of(), List.of(), false, false);

        inappropriateEvents.forEach(event -> deleteFromEvent(event.getKey(), accessChecker.getUserId(), ParticipantStatus.INAPPROPRIATE));
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

    public boolean checkParticipating(String key) {
        return getParticipants(key, List.of(OK)).stream().anyMatch(x -> x.getProfile().getUserId().equals(accessChecker.getUserId()));
    }
}
