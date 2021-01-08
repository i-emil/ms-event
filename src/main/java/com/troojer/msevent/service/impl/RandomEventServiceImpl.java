package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ParticipantClient;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.RandomEventEntity;
import com.troojer.msevent.dao.repository.RandomEventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.mapper.ParticipantMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.enm.ParticipantType;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NoContentExcepton;
import com.troojer.msevent.service.EventService;
import com.troojer.msevent.service.RandomEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RandomEventServiceImpl implements RandomEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RandomEventRepository randomEventRepository;

    private final EventMapper eventMapper;
    private final EventService eventService;

    private final ProfileClient profileClient;
    private final ParticipantClient participantClient;

    private final AccessCheckerUtil accessChecker;

    public RandomEventServiceImpl(EventMapper eventMapper, EventService eventService, ProfileClient profileClient, RandomEventRepository randomEventRepository, ParticipantClient participantClient, AccessCheckerUtil accessChecker) {
        this.eventMapper = eventMapper;
        this.eventService = eventService;
        this.profileClient = profileClient;
        this.randomEventRepository = randomEventRepository;
        this.participantClient = participantClient;
        this.accessChecker = accessChecker;
    }

    @Override
    public EventDto getEvent(int days) {
        FilterDto filter = profileClient.getProfileFilter();
        Optional<RandomEventEntity> pendingRandomEvent = getCurrentPendingRandomEvent();
        if (pendingRandomEvent.isPresent()) {
            RandomEventEntity randomEventEntity = pendingRandomEvent.get();
            Long eventForCheckId = randomEventEntity.getEvent().getId();
            List<EventEntity> checkEvent = eventService.getEventsByFilter(List.of(eventForCheckId), filter, days, getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
            if (!checkEvent.isEmpty())
                return eventMapper.randomEventEntityToEventDto(pendingRandomEvent.get());
            else {
                randomEventEntity.setStatus(UserFoundEventStatus.IGNORED);
                randomEventRepository.save(randomEventEntity);
            }
        }

        List<EventEntity> eventsByFilter = eventService.getEventsByFilter(new ArrayList<>(), filter, days, getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
        if (eventsByFilter.isEmpty()) throw new NoContentExcepton("event.random.notFound");

        EventEntity eventEntity = getRandomEventFromList(eventsByFilter);
        RandomEventEntity randomEventEntity = RandomEventEntity.create(accessChecker.getUserId(), eventEntity);
        randomEventRepository.save(randomEventEntity);
        logger.info("getEvent(); save to userFoundEvent: {}", randomEventEntity);

        return eventMapper.randomEventEntityToEventDto(randomEventEntity);
    }

    @Override
    @Transactional
    public void accept(String key) {
        RandomEventEntity randomEventEntity = getUserFoundEventByKey(key);
        EventEntity pendingEvent = randomEventEntity.getEvent();
        FilterDto filter = profileClient.getProfileFilter();
        List<EventEntity> checkEvent = eventService.getEventsByFilter(List.of(pendingEvent.getId()), filter, 30, getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
        if (checkEvent.isEmpty()) throw new ForbiddenException("event.accept.notAvailable");

        Optional<ParticipantType> participantType = eventService.raisePersonCountAndGetType(pendingEvent, Gender.valueOf(filter.getGender()));
        ParticipantDto participant = ParticipantMapper.foundEventToParticipant(randomEventEntity, participantType.orElseThrow(() -> new ForbiddenException("event.accept.notAvailable")));

        randomEventEntity.setStatus(UserFoundEventStatus.ACCEPTED);
        randomEventRepository.save(randomEventEntity);
        logger.info("accept(); event accepted: {}", pendingEvent);
        participantClient.addParticipant(participant);
        logger.info("accept(); added to participant: {}", participant);
    }

    @Override
    public void reject(String key) {
        RandomEventEntity randomEventEntity = getUserFoundEventByKey(key);
        randomEventEntity.setStatus(UserFoundEventStatus.REJECTED);
        randomEventRepository.save(randomEventEntity);
        logger.info("refuse(); event rejected: {}", randomEventEntity.getEvent());
    }

    @Override
    @Transactional
    public void deleteOld(LocalDateTime beforeDateTime) {
        randomEventRepository.deleteAll(randomEventRepository.getAllByUpdatedAtBefore(beforeDateTime));
        logger.info("deleteOld(); deleted before {}", beforeDateTime);
    }

    @Override
    public void deleteInappropriate() {
//        List<ParticipantDto> participantDtos = participantClient.getEvents();
//        List<Long> eventForCheckId = participantDtos.stream().map(ParticipantDto::getEventId).collect(Collectors.toList());
//        if (eventForCheckId.isEmpty()) return;
//        FilterDto filter = profileClient.getProfileFilter();
//        List<Long> availableEventsId = eventService.getEventsByFilter(eventForCheckId, filter, 30, getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged()).stream().map(EventEntity::getId).collect(Collectors.toList());
//        participantDtos.stream().filter(p -> !availableEventsId.contains(p.getEventId())).forEach(p -> {
//            participantClient.removeParticipant(p.getEventId());
//            //todo change event participants count
//        });
    }

    private Optional<RandomEventEntity> getCurrentPendingRandomEvent() {
        return randomEventRepository.getFirstByUserIdAndStatusIn(accessChecker.getUserId(), List.of(UserFoundEventStatus.PENDING));
    }

    private RandomEventEntity getUserFoundEventByKey(String key) {
        Optional<RandomEventEntity> randomEventEntityOptional = randomEventRepository.getFirstByIdAndUserIdAndStatusIn(key, accessChecker.getUserId(), List.of(UserFoundEventStatus.PENDING));
        return randomEventEntityOptional.orElseThrow(() -> new ForbiddenException("event.key.wrong"));
    }

    private List<Long> getUserAcceptedAndRejectedEventIdList(String userId) {
        return randomEventRepository
                .getAllByUserIdAndStatusIn(userId, List.of(UserFoundEventStatus.ACCEPTED, UserFoundEventStatus.REJECTED))
                .stream().map(x -> x.getEvent().getId())
                .collect(Collectors.toList());
    }

    private EventEntity getRandomEventFromList(List<EventEntity> eventList) {
        int index = new Random().nextInt(eventList.size());
        return eventList.get(index);
    }

}
