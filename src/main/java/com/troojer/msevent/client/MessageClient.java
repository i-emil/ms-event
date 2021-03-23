package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.MessageDto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MessageClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    @Value("${client.message.url}")
    private String url;

    public MessageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addMessage(MessageDto message) {
        restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(message), Void.TYPE);
        logger.debug("addMessage: {}", message);
    }
}
