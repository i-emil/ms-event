package com.troojer.msevent.service.impl;

import ch.qos.logback.classic.Logger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.troojer.msevent.model.NotificationDto;
import com.troojer.msevent.service.MqService;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service
public class MqServiceRabbitImpl implements MqService {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private final RabbitTemplate rabbitTemplate;

    public MqServiceRabbitImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void sendNotificationToQueue(NotificationDto message) {
        try {
            String str = new ObjectMapper().writeValueAsString(message);
            logger.debug("sendMailToQueue(), {}", str);
            rabbitTemplate.convertAndSend("innerNotificationQueue", str);
        } catch (JsonProcessingException e) {
            logger.warn("sendNotificationToQueue(), exc: ", e);
        }
    }
}