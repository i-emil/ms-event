package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.client.MessageClient;
import com.troojer.msevent.dao.EventEntity;
import com.troojer.msevent.dao.EventMessageEntity;
import com.troojer.msevent.dao.repository.EventMessageRepository;
import com.troojer.msevent.mapper.EventMessageMapper;
import com.troojer.msevent.model.EventMessageDto;
import com.troojer.msevent.model.MessageDto;
import com.troojer.msevent.model.enm.MessageType;
import com.troojer.msevent.model.exception.ForbiddenException;
import com.troojer.msevent.service.EventMessageService;
import com.troojer.msevent.service.InnerEventService;
import com.troojer.msevent.service.ParticipantService;
import com.troojer.msevent.util.AccessCheckerUtil;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.troojer.msevent.model.enm.ParticipantStatus.OK;

@Service
public class EventMessageServiceImpl implements EventMessageService {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final EventMessageRepository eventMessageRepository;
    private final EventMessageMapper eventMessageMapper;
    private final InnerEventService innerEventService;
    private final AccessCheckerUtil accessChecker;
    private final ParticipantService participantService;
    private final MessageClient messageClient;


    public EventMessageServiceImpl(EventMessageRepository eventMessageRepository, EventMessageMapper eventMessageMapper, InnerEventService innerEventService, AccessCheckerUtil accessChecker, ParticipantService participantService, MessageClient messageClient) {
        this.eventMessageRepository = eventMessageRepository;
        this.eventMessageMapper = eventMessageMapper;
        this.innerEventService = innerEventService;
        this.accessChecker = accessChecker;
        this.participantService = participantService;
        this.messageClient = messageClient;
    }

    @Override
    public void addMessage(String eventKey, EventMessageDto eventMessageDto) {
        EventEntity eventEntity = checkAccessAndGetEventId(eventKey);
        EventMessageEntity messageEntity = eventMessageMapper.dtoToEntity(eventMessageDto, eventEntity.getId());
        eventMessageRepository.save(messageEntity);
        List<String> usersId = participantService.getParticipants(eventKey, List.of(OK)).stream().map(x -> x.getProfile().getUserId()).collect(Collectors.toList());
        usersId.remove(accessChecker.getUserId());
        messageClient.addMessage(MessageDto.builder().type(MessageType.EVENT_MESSAGE).title("New message - " + eventEntity.getTitle()).message(eventMessageDto.getMessage()).usersId(usersId).build());
    }

    @Override
    public List<EventMessageDto> getMessages(String eventKey, Pageable pageable) {
        EventEntity eventEntity = checkAccessAndGetEventId(eventKey);
        return eventMessageMapper.entitiesToDtos(eventMessageRepository.getAllByEventId(eventEntity.getId(), pageable));
    }

    private EventEntity checkAccessAndGetEventId(String eventKey) {
        Optional<EventEntity> eventEntityOpt = innerEventService.getEventEntity(eventKey);
        if (eventEntityOpt.isEmpty() || !participantService.checkParticipating(eventKey)) {
            logger.warn("addMessage: It's not user's event or event not exist; eventKey: {};", eventKey);
            throw new ForbiddenException("event.event.forbidden");
        }
        return eventEntityOpt.get();
    }
}
