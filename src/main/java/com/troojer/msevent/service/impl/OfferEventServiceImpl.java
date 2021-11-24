package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ProfileClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.mapper.DatesMapper;
import com.troojer.msevent.mapper.EventMapper;
import com.troojer.msevent.model.EventDto;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.ProfileInfo;
import com.troojer.msevent.model.enm.Gender;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.NoContentExcepton;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.OfferEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class OfferEventServiceImpl implements OfferEventService {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventMapper eventMapper;
    private final InnerEventService innerEventService;

    private final ProfileClient profileClient;
    private final ParticipantService participantService;

    private final AccessCheckerUtil accessChecker;

    @Override
    public List<EventDto> getOfferEvents(FilterDto filter) {
        ZonedDateTime start = DatesMapper.dtoToEntity(filter.getDates().getStart());
        if (start.isBefore(ZonedDateTime.now())) start = ZonedDateTime.now();
        ZonedDateTime end = DatesMapper.dtoToEntity(filter.getDates().getEnd());
        ProfileInfo profileInfo = profileClient.getProfileFilter();

        List<EventEntity> allEvents = innerEventService.getEventsByDateAndStatus(start, end, List.of(ACTIVE));
        List<EventEntity> filteredEvents = innerEventService.getEventsByFilter(allEvents, filter.getTagIdList(), profileInfo.getCurrentAge(), profileInfo.getGender(), List.of(), List.of(accessChecker.getUserId()), true, true);
        if (filteredEvents.isEmpty()) throw new NoContentExcepton("event.offer.notFound");

        return eventMapper.entitiesToDtos(filteredEvents);
    }

    @Override
    public void accept(String eventKey) {
        EventEntity eventEntity = innerEventService.getEventEntity(eventKey).orElseThrow(() -> new NotFoundException("event.event.notFound"));

        Integer currentAge = null;
        Gender gender = null;

        if (!eventEntity.isFilterDisabled()) {
            ProfileInfo profileInfo = profileClient.getProfileFilter();
            currentAge = profileInfo.getCurrentAge();
            gender = profileInfo.getGender();
        }

        List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(eventEntity), null, currentAge, gender, List.of(), List.of(accessChecker.getUserId()), false, false);

        if (checkEvent.isEmpty()) {
            throw new ConflictException("event.accept.notAvailable");
        } else {
            joinEvent(checkEvent.get(0), gender);
        }
    }

    private void joinEvent(EventEntity eventEntity, Gender gender) {
        participantService.joinEvent(eventEntity.getKey(), accessChecker.getUserId(), gender);
        logger.info("accept(); event accepted: {}", eventEntity);
    }
}
