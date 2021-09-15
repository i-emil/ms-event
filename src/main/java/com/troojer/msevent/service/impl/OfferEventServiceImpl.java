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
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.OfferEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
        ZonedDateTime end = DatesMapper.dtoToEntity(filter.getDates().getEnd());
        ProfileInfo profileInfo = profileClient.getProfileFilter();
        filter.setProfileInfo(profileInfo);

        List<EventEntity> eventsByFilter = innerEventService.getEventsByFilter(new ArrayList<>(), filter, start, end, List.of(ACTIVE), List.of(), List.of(accessChecker.getUserId()), true, Pageable.unpaged());
        if (eventsByFilter.isEmpty()) throw new NoContentExcepton("event.offer.notFound");

        return eventMapper.entitiesToDtos(eventsByFilter);
    }

    @Override
    public void accept(String eventKey) {
        FilterDto filter = new FilterDto();
        filter.setProfileInfo(profileClient.getProfileFilter());

        List<EventEntity> checkEvent = innerEventService.getEventsByFilter(List.of(eventKey), filter, ZonedDateTime.now().plusMinutes(30), ZonedDateTime.now().plusMonths(1), List.of(ACTIVE), List.of(), List.of(accessChecker.getUserId()), true, Pageable.unpaged());

        if (checkEvent.isEmpty()) {
            throw new ConflictException("event.accept.notAvailable");
        } else {
            joinEvent(checkEvent.get(0), filter.getGender());
        }
    }

    private void joinEvent(EventEntity eventEntity, Gender gender) {
        participantService.joinEvent(eventEntity.getKey(), accessChecker.getUserId(), gender);
        logger.info("accept(); event accepted: {}", eventEntity);
    }
}
