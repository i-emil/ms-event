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

import java.util.List;
import java.util.Set;

@Component
public class TagClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("${client.tag.url}")
    private String url;

    public TagClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("tagList")
    public Set<TagDto> getAllTags() {
        try {
            ParameterizedTypeReference<Set<TagDto>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<Set<TagDto>> responseEntity = restTemplate.exchange(url + "en", HttpMethod.GET, null, responseType);
            logger.info("getOrAddTags(); client response: {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            logger.warn("getOrAddTags(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }
}
