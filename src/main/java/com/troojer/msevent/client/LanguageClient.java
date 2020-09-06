package com.troojer.msevent.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class LanguageClient {
    private final RestTemplate restTemplate;

    @Value("${client.localization.url}")
    private String url;

    public LanguageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("languagesCodes")
    public Map<String, String> getLanguagesMap() {
        ParameterizedTypeReference<Map<String, String>> responseType =
                new ParameterizedTypeReference<>() {};
        return restTemplate.exchange(url + "localization/language/map", HttpMethod.GET, null, responseType).getBody();
    }
}
