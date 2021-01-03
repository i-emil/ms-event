package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.TagDto;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TagClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("${client.tag.url}")
    private String url;

    public TagClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Set<TagDto> getAllByIds(Set<Long> ids) {
        Set<String> stringIdSet = ids.stream().map(String::valueOf).collect(Collectors.toSet());
        String uri = url + "id/" + String.join(",", stringIdSet);
        try {
            ParameterizedTypeReference<Set<TagDto>> responseType =
                    new ParameterizedTypeReference<>() {
                    };
            ResponseEntity<Set<TagDto>> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, HttpEntity.EMPTY, responseType);
            logger.info("getAllByIds(); client response: {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            logger.warn("getLanguagesMap(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }

    public Set<TagDto> getOrAddTags(Set<TagDto> dtoSet) {
        try {
            ParameterizedTypeReference<Set<TagDto>> responseType = new ParameterizedTypeReference<>() {
            };
            ResponseEntity<Set<TagDto>> responseEntity = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(dtoSet), responseType);
            logger.info("getOrAddTags(); client response: {}", responseEntity);
            return responseEntity.getBody();
        } catch (Exception e) {
            logger.warn("getOrAddTags(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }
}
