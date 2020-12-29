package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ParticipantClient;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.UserFoundEventEntity;
import com.troojer.msevent.dao.repository.UserFoundEventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.mapper.ParticipantMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.service.FindEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FindEventServiceImpl implements FindEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final UserFoundEventRepository userFoundEventRepository;

    private final EventMapper eventMapper;
    private final EventService eventService;

    private final ProfileClient profileClient;
    private final ParticipantClient participantClient;

    private final AccessCheckerUtil accessChecker;

    public FindEventServiceImpl(EventMapper eventMapper, EventService eventService, ProfileClient profileClient, UserFoundEventRepository userFoundEventRepository, ParticipantClient participantClient, AccessCheckerUtil accessChecker) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
        this.profileClient = profileClient;
        this.userFoundEventRepository = userFoundEventRepository;
        this.participantClient = participantClient;
        this.accessChecker = accessChecker;
    }

    @Override
    public EventDto getEvent(int days) {
        FilterDto filterDto = profileClient.getProfileFilter();
        logger.info("getRandomEvent(); filterDto: {}", filterDto);
        EventEntity eventByFilter = eventService.getEventByFilter(filterDto, days, getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()));
        Map<String, UserFoundEventEntity> userPendingEvents = getUserPendingEvents();

        UserFoundEventEntity userFoundEventEntity = (userPendingEvents.get(eventByFilter.getId()) == null) ?
                UserFoundEventEntity.create(accessChecker.getUserId(), eventByFilter) : userPendingEvents.get(eventByFilter.getId());
        userFoundEventRepository.save(userFoundEventEntity);

        logger.info("getRandomEvent(); save to userFoundEvent: {}", userFoundEventEntity);
        return eventMapper.entityToDtoWithKey(eventByFilter, userFoundEventEntity.getKey());
    }

    @Override
    @Transactional
    public void accept(String key) {
        UserFoundEventEntity userFoundEventEntity = getUserFoundEventByKey(key);
        EventEntity userLastEvent = userFoundEventEntity.getEvent();
        FilterDto profileFilter = profileClient.getProfileFilter();
        if (!isDateValid(userLastEvent)) throw new ForbiddenException("event.accept.timeOut::15");
        ParticipantType participantType;

        userFoundEventEntity.setStatus(UserFoundEventStatus.ACCEPTED);
        userFoundEventRepository.save(userFoundEventEntity);
        logger.info("accept(); event accepted: {}", userLastEvent);
        //todo
        ParticipantDto participant = ParticipantMapper.foundEventToParticipant(userFoundEventEntity, ParticipantType.ALL);
        participantClient.addParticipant(participant);
        logger.info("accept(); added to participant: {}", participant);
    }

    @Override
    public void reject(String key) {
        UserFoundEventEntity userFoundEventEntity = getUserFoundEventByKey(key);
        userFoundEventEntity.setStatus(UserFoundEventStatus.REJECTED);
        userFoundEventRepository.save(userFoundEventEntity);
        logger.info("refuse(); event rejected: {}", userFoundEventEntity.getEvent());
    }

    @Override
    @Transactional
    public void deleteOld(LocalDateTime beforeDateTime) {
        userFoundEventRepository.deleteAll(userFoundEventRepository.getAllByUpdatedAtBefore(beforeDateTime));
        logger.info("deleteOld(); deleted before {}", beforeDateTime);
    }

    @Override
    public void deleteInappropriate() {
        //todo
    }

    private Map<String, UserFoundEventEntity> getUserPendingEvents() {
        return userFoundEventRepository.getAllByUserIdAndStatusIn(accessChecker.getUserId(), List.of(UserFoundEventStatus.PENDING))
                .stream().collect(Collectors.toMap(x -> x.getEvent().getId(), x -> x));
    }

    private UserFoundEventEntity getUserFoundEventByKey(String key) {
        List<UserFoundEventEntity> userFoundEventEntityList = userFoundEventRepository.getFirstByUserIdAndStatusInAndKey(accessChecker.getUserId(), List.of(UserFoundEventStatus.PENDING), key);
        if (userFoundEventEntityList.isEmpty()) throw new ForbiddenException("event.key.wrong");
        return userFoundEventEntityList.get(0);
    }

    private List<String> getUserAcceptedAndRejectedEventIdList(String userId) {
        return userFoundEventRepository
                .getAllByUserIdAndStatusIn(userId, List.of(UserFoundEventStatus.ACCEPTED, UserFoundEventStatus.REJECTED))
                .stream().map(x -> x.getEvent().getId())
                .collect(Collectors.toList());
    }

    private boolean isDateValid(EventEntity eventEntity) {
        return Duration.between(LocalDateTime.now(), eventEntity.getStartDate().toLocalDateTime()).toMinutes() >= 15;
    }

}
