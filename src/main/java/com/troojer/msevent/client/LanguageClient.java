package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class LanguageClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("${client.localization.url}")
    private String url;

    public LanguageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("languagesCodes")
    public Map<String, String> getLanguagesMap() {
        try {
            ParameterizedTypeReference<Map<String, String>> responseType =
                    new ParameterizedTypeReference<>() {
                    };
            return restTemplate.exchange(url + "languages/map", HttpMethod.GET, null, responseType).getBody();
        } catch (Exception e) {
            logger.warn("getLanguagesMap(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }
}
