package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.client.LocationClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.mapper.BudgetMaper;
import com.troojer.msevent.mapper.StartEndDatesMapper;
import com.troojer.msevent.mapper.TagMapper;
import com.troojer.msevent.model.*;
import com.troojer.msevent.model.enm.EventStatus;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.model.exception.NotFoundException;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.MangeEventService;
import com.troojer.msevent.service.MqService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.troojer.msevent.model.InnerNotificationType.EVENT_CHANGE;
import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;
import static com.troojer.msevent.model.enm.ParticipantStatus.OK;

@Service
public class ManageEventServiceImpl implements MangeEventService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final ImageClient imageClient;
    private final InnerEventService innerEventService;
    private final AccessCheckerUtil accessChecker;
    private final BudgetMaper budgetMaper;
    private final TagMapper tagMapper;
    private final LocationClient locationClient;
    private final MqService mqService;
    private final ParticipantService participantService;

    public ManageEventServiceImpl(ImageClient imageClient, InnerEventService innerEventService, AccessCheckerUtil accessChecker, BudgetMaper budgetMaper, TagMapper tagMapper, LocationClient locationClient, MqService mqService, ParticipantService participantService) {
        this.imageClient = imageClient;
        this.innerEventService = innerEventService;
        this.accessChecker = accessChecker;
        this.budgetMaper = budgetMaper;
        this.tagMapper = tagMapper;
        this.locationClient = locationClient;
        this.mqService = mqService;
        this.participantService = participantService;
    }

    @Override
    public String updateEventTitle(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        String oldTitle = eventEntity.getTitle();
        String title = eventDto.getTitle().toLowerCase();
        logger.info("updateEventTitle(); old: {}", eventEntity.getTitle());
        eventEntity.setTitle(title);
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventTitle.title", "change.eventTitle.description::" + oldTitle);
        return title;
    }

    @Override
    public String updateEventDescription(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        String description = eventDto.getDescription().toLowerCase();
        logger.info("updateEventDescription(); old: {}", eventEntity.getDescription());
        eventEntity.setTitle(description);
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventDescription.title", "change.eventDescription.description::" + eventEntity.getTitle());
        return description;
    }

    @Override
    public BudgetDto updateEventBudget(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        BudgetDto budgetDto = eventDto.getBudget();
        logger.info("updateEventBudget(); old: {} {}", eventEntity.getBudget(), eventEntity.getCurrency());
        eventEntity.setBudget(budgetDto.getAmount());
        eventEntity.setCurrency(budgetDto.getCurrency().getCode());
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventBudget.title", "change.eventBudget.description::" + eventEntity.getTitle());
        return budgetMaper.eventToBudgetDto(eventEntity);
    }

    @Override
    public String updateEventCover(String key, EventDto eventDto) {
        imageClient.isImageExist(eventDto.getCover());
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        String cover = eventDto.getCover();
        String oldCoverId = eventEntity.getCover();
        logger.info("updateEventCover(); old: {}", oldCoverId);
        eventEntity.setCover(cover);
        if (!cover.equals(eventEntity.getCover())) {
            innerEventService.saveOrUpdateEntity(eventEntity);
//        imageClient.deleteImage(oldCoverId);
        }
        notifyAboutChanges(eventEntity, "change.eventCover.title", "change.eventCover.description::" + eventEntity.getTitle());
        return imageClient.getImageUrl(cover);
    }

    @Override
    public Set<TagDto> updateEventTags(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        Set<TagDto> tags = eventDto.getTags();
        logger.info("updateEventTags(); old: {}", eventEntity.getTags());
        eventEntity.setTags(tagMapper.dtoSetToEntitySet(tags, eventEntity));
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventTags.title", "change.eventTags.description::" + eventEntity.getTitle());
        return tagMapper.entitySetToDtoSet(eventEntity.getTags());
    }

    @Override
    public InvitingDto updateEventInviting(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        InvitingDto invitingDto = eventDto.getInviting();
        logger.info("updateEventInviting(); old: {}", eventEntity.isInviteActive());
        eventEntity.setInviteActive(invitingDto.isActive());
        eventEntity.setInvitePassword(invitingDto.getPassword());
        innerEventService.saveOrUpdateEntity(eventEntity);
        invitingDto.setKey(eventEntity.getKey());
        return invitingDto;
    }

    @Override
    public StartEndDatesDto updateEventDate(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        StartEndDatesDto startEndDatesDto = eventDto.getDate();
        logger.info("updateEventDate(); old: {} - {}", eventEntity.getStartDate(), eventEntity.getEndDate());
        if (startEndDatesDto.getStart() != null)
            eventEntity.setStartDate(StartEndDatesMapper.dtoToStartDate(startEndDatesDto));
        if (startEndDatesDto.getEnd() != null)
            eventEntity.setEndDate(StartEndDatesMapper.dtoToEndDate(startEndDatesDto));
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventDate.title", "change.eventDate.description::" + eventEntity.getTitle());
        return StartEndDatesMapper.entityDatesToDto(eventEntity.getStartDate(), eventEntity.getEndDate());
    }

    @Override
    public LocationDto updateEventLocation(String key, EventDto eventDto) {
        locationClient.getLocation(eventDto.getLocation().getId());
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        LocationDto locationDto = eventDto.getLocation();
        logger.info("updateEventLocation(); old: {}", eventEntity.getLocationId());
        eventEntity.setLocationId(locationDto.getId());
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventLocation.title", "change.eventLocation.description::" + eventEntity.getTitle());
        return locationClient.getLocation(eventEntity.getLocationId());
    }

    @Override
    public void deleteEvent(String key) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
//        imageClient.deleteImage(eventEntity.getCover());
        eventEntity.setStatus(EventStatus.CANCELED);
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "delete.event.title", "delete.event.description::" + eventEntity.getTitle());
        logger.info("deleteEvent(); eventId:{}", key);
    }

    private EventEntity getEventEntity(String key) {
        EventEntity event = innerEventService.getEventEntity(key)
                .orElseThrow(() -> new NotFoundException("event.event.notFound"));
        if (!accessChecker.isUserId(event.getAuthorId()) || event.getStatus() != ACTIVE) {
            throw new NotFoundException("event.event.notFound");
        }
        return event;
    }

    private void checkEventChangeable(ZonedDateTime startDate) {
        if (startDate.minusHours(3).isBefore(ZonedDateTime.now()))
            throw new ForbiddenException("event.change.forbidden::3");
    }

    private void notifyAboutChanges(EventEntity event, String title, String description) {
        participantService.getParticipants(event.getKey(), List.of(OK))
                .forEach(p -> {
                    if (!event.getAuthorId().equals(p.getProfile().getUserId()))
                        mqService.sendNotificationToQueue(NotificationDto.builder().userId(p.getProfile().getUserId()).title(title).description(description).params(Map.of("eventKey", event.getKey())).type(EVENT_CHANGE).build());
                });
    }
}
