package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.ImageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.mapper.BudgetMaper;
import com.troojer.msevent.mapper.DatesMapper;
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
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.troojer.msevent.model.enm.EventStatus.ACTIVE;
import static com.troojer.msevent.model.enm.MessageType.EVENT_CHANGE;
import static com.troojer.msevent.model.enm.ParticipantStatus.OK;

@Service
@RequiredArgsConstructor
public class ManageEventServiceImpl implements MangeEventService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final ImageClient imageClient;
    private final InnerEventService innerEventService;
    private final AccessCheckerUtil accessChecker;
    private final BudgetMaper budgetMaper;
    private final TagMapper tagMapper;
    private final MqService mqService;
    private final ParticipantService participantService;
    private final EventDataChecker eventDataChecker;

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
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        eventDataChecker.checkCover(eventDto.getCover());
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
        eventDataChecker.checkTags(eventDto.getTags());
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
    public String updateEventDate(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        String startDate = eventDto.getStartDate();
        logger.info("updateEventDate(); old: {}", eventEntity.getStartDate());
        if (startDate != null)
            eventEntity.setStartDate(DatesMapper.dtoToEntity(startDate));
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventDate.title", "change.eventDate.description::" + eventEntity.getTitle());
        return startDate;
    }

    @Override
    public Integer updateEventDuration(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        Integer duration = eventDto.getDuration();
        logger.info("updateEventDate(); old: {}", eventEntity.getStartDate());
        if (duration != null)
            eventEntity.setDuration(duration);
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.duration.title", "change.duration.description::" + eventEntity.getTitle());
        return duration;
    }

    @Override
    public Optional<String> updateEventLocation(String key, EventDto eventDto) {
        EventEntity eventEntity = getEventEntity(key);
        checkEventChangeable(eventEntity.getStartDate());
        eventDataChecker.checkLocation(eventDto.getLocationId());
        logger.info("updateEventLocation(); old: {}", eventEntity.getLocationId());
        eventEntity.setLocationId(eventDto.getLocationId());
        innerEventService.saveOrUpdateEntity(eventEntity);
        notifyAboutChanges(eventEntity, "change.eventLocation.title", "change.eventLocation.description::" + eventEntity.getTitle());
        return Optional.of(eventDto.getLocationId());
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
        List<String> recipientsId = participantService.getParticipants(event.getKey(), List.of(OK)).stream().map(p -> p.getProfile().getUserId()).collect(Collectors.toList());
        recipientsId.remove(accessChecker.getUserId());
        mqService.sendNotificationToQueue(NotificationDto.builder().recipientsId(recipientsId).title(title).description(description).params(Map.of("eventKey", event.getKey())).type(EVENT_CHANGE).build());
    }
}
