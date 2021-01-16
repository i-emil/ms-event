package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.RandomEventEntity;
import com.troojer.msevent.dao.repository.RandomEventRepository;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.enm.UserFoundEventStatus;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NoContentExcepton;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.OuterEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.service.RandomEventService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;

@Service
public class RandomEventServiceImpl implements RandomEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RandomEventRepository randomEventRepository;

    private final EventMapper eventMapper;
    private final InnerEventService innerEventService;

    private final ProfileClient profileClient;
    private final ParticipantService participantService;

    private final AccessCheckerUtil accessChecker;

    public RandomEventServiceImpl(EventMapper eventMapper, ProfileClient profileClient, RandomEventRepository randomEventRepository, InnerEventService innerEventService, OuterEventService outerEventService, ParticipantService participantService, AccessCheckerUtil accessChecker) {
        this.eventMapper = eventMapper;
        this.profileClient = profileClient;
        this.randomEventRepository = randomEventRepository;
        this.innerEventService = innerEventService;
        this.participantService = participantService;
        this.accessChecker = accessChecker;
    }

    @Override
    public EventDto getEvent(int days) {
        FilterDto filter = profileClient.getProfileFilter();
        Optional<RandomEventEntity> pendingRandomEvent = getCurrentPendingRandomEvent();
        if (pendingRandomEvent.isPresent()) {
            RandomEventEntity randomEventEntity = pendingRandomEvent.get();
            Long eventIdForCheck = randomEventEntity.getEvent().getId();
            List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(eventIdForCheck), filter, ZonedDateTime.now().plusMinutes(30), ZonedDateTime.now().plusDays(days), List.of(ACTIVE), getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
            if (!checkEvent.isEmpty())
                return eventMapper.randomEventEntityToEventDto(pendingRandomEvent.get());
            else {
                randomEventEntity.setStatus(UserFoundEventStatus.NO_AVAILABLE);
                randomEventRepository.save(randomEventEntity);
            }
        }

        List<EventEntity> eventsByFilter = innerEventService.getEventsByFilter(new ArrayList<>(), filter, ZonedDateTime.now().plusMinutes(30), ZonedDateTime.now().plusDays(days), List.of(ACTIVE), getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
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
        if (pendingEvent != null) {
            FilterDto filter = profileClient.getProfileFilter();
            List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(pendingEvent.getId()), filter, ZonedDateTime.now().plusMinutes(30), ZonedDateTime.now().plusMonths(1), List.of(ACTIVE), getUserAcceptedAndRejectedEventIdList(accessChecker.getUserId()), Pageable.unpaged());
            if (!checkEvent.isEmpty()) {
                joinEvent(pendingEvent, randomEventEntity);
                return;
            }
        }
        randomEventEntity.setStatus(UserFoundEventStatus.NO_AVAILABLE);
        randomEventRepository.save(randomEventEntity);
        logger.warn("accept(); this event not available now: {}", pendingEvent);
        throw new ForbiddenException("event.accept.notAvailable");
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

    private void joinEvent(EventEntity pendingEvent, RandomEventEntity randomEventEntity) {
        participantService.joinEvent(pendingEvent.getKey(), accessChecker.getUserId());
        randomEventEntity.setStatus(UserFoundEventStatus.ACCEPTED);
        randomEventRepository.save(randomEventEntity);
        logger.info("accept(); event accepted: {}", randomEventEntity);
    }
}
