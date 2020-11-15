package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.ParticipantDto;
import com.troojer.msevent.model.exception.ClientException;
import com.troojer.msevent.model.exception.ConflictException;
import com.troojer.msevent.model.exception.ForbiddenException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Component
public class ParticipantClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("${client.participant.url}")
    private String url;

    public ParticipantClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<ParticipantDto> getParticipants(String eventId) {
        try {
            ResponseEntity<ParticipantDto[]> forEntity = restTemplate.getForEntity(url + eventId, ParticipantDto[].class);
            return List.of(Objects.requireNonNull(forEntity.getBody()));
        } catch (Exception exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw new ClientException("default.service.temporaryError");
        }
    }

    public void addParticipant(ParticipantDto participantDto) {
        try {
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(participantDto), Void.TYPE);
            logger.info("addParticipant(); done");
        } catch (RestClientResponseException exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw switch (exc.getRawStatusCode()) {
                case 403 -> new ForbiddenException(exc.getMessage());
                case 409 -> new ConflictException(exc.getMessage());
                default -> new ClientException("default.service.temporaryError");
            };
        } catch (Exception e) {
            logger.warn("addParticipant(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }
}
