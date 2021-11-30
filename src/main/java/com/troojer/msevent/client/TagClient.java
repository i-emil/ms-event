package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.TagDto;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;

@Component
public class TagClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private LocalDateTime tagLastUpdated;
    private Set<TagDto> localTagSet;

    @Value("${client.tag.url}")
    private String url;

    public TagClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Set<TagDto> getAllTags() {
        try {
            if (tagLastUpdated != null && Duration.between(tagLastUpdated, LocalDateTime.now()).toMinutes() < 30)
                return this.localTagSet;
            ParameterizedTypeReference<Set<TagDto>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<Set<TagDto>> responseEntity = restTemplate.exchange(url + "en", HttpMethod.GET, null, responseType);
            logger.info("getOrAddTags(); client response: {}", responseEntity);
            updateLocalTags(responseEntity.getBody());
            return responseEntity.getBody();
        } catch (Exception e) {
            logger.warn("getOrAddTags(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }

    public void updateLocalTags(Set<TagDto> tagSet) {
        if (tagSet != null && !tagSet.isEmpty()) {
            this.localTagSet = tagSet;
            this.tagLastUpdated = LocalDateTime.now();
        }
    }
}
