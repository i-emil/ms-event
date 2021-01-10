package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventParticipantTypeEntity;
import com.troojer.msevent.dao.ParticipantEntity;
import com.troojer.msevent.dao.repository.ParticipantRepository;
import com.troojer.msevent.mapper.ParticipantMapper;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantStatus;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.service.ParticipantService;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.troojer.msevent.model.enm.ParticipantStatus.*;
import static com.troojer.msevent.model.enm.ParticipantType.ALL;
import static com.troojer.msevent.model.enm.ParticipantType.COUPLE;

@Service
public class ParticipantServiceImpl implements ParticipantService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final ParticipantRepository participantRepository;
    private final EventService eventService;
    private final ParticipantMapper participantMapper;

    public ParticipantServiceImpl(ParticipantRepository participantRepository, EventService eventService, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.eventService = eventService;
        this.participantMapper = participantMapper;
    }

    @Override
    public List<ParticipantDto> getParticipants(String eventKey, List<ParticipantStatus> status) {
        return participantMapper.entitiesToDtos(participantRepository.getParticipants(eventKey, status));
    }

    @Override
    public boolean addParticipant(String eventKey, String userId, ParticipantType userType) {
        Optional<EventParticipantTypeEntity> eventParticipantTypeOpt = getEventParticipantEntityByUserParticipantType(eventKey, userType);
        if (eventParticipantTypeOpt.isPresent()) {
            EventParticipantTypeEntity eventParticipantType = eventParticipantTypeOpt.get();
            EventEntity event = eventParticipantType.getEvent();
            event.getParticipants().add(ParticipantEntity.builder().userId(userId).event(event).type(eventParticipantType.getType()).build());
            eventParticipantType.increaseAccepted();
            eventService.saveOrUpdateEntity(event);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public boolean deleteParticipant(String eventKey, String userId, ParticipantStatus reason) {
        if (reason == DELETED_BY_AUTHOR || reason == LEFT || reason == INAPPROPRIATE) {
            try {
                EventEntity event = eventService.getEventEntity(eventKey);
                Optional<ParticipantEntity> participantOpt = participantRepository.getFirstByEventIdAndUserIdAndStatusIn(event.getId(), userId, List.of(OK));
                participantOpt.ifPresent(p -> {
                    p.setStatus(reason);
                    event.getParticipantsType().get(p.getType()).decreaseAccepted();
                    participantRepository.save(p);
                    eventService.saveOrUpdateEntity(event);
                });
                return true;
            } catch (Exception e) {
                logger.warn("deleteParticipant(); exc: ", e);
            }
            return false;
        }
        throw new IllegalArgumentException("deleting reason is illegal");
    }

    private Optional<EventParticipantTypeEntity> getEventParticipantEntityByUserParticipantType(String eventKey, ParticipantType userType) {
        EventEntity event;
        try {
            event = eventService.getEventEntity(eventKey);
            Map<ParticipantType, EventParticipantTypeEntity> participantTypeMap = event.getParticipantsType();

            if (userType.equals(COUPLE)) {
                if (participantTypeMap.containsKey(userType) && participantTypeMap.get(userType).isFree())
                    return Optional.of(participantTypeMap.get(userType));
            } else {
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
}
