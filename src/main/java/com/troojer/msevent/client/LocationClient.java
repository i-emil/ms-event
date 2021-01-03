package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.LocationDto;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LocationClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    @Value("${client.location.url}")
    private String url;

    public LocationClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public LocationDto getLocation(String locationId) {
        try {
            return restTemplate.getForEntity(url + locationId, LocationDto.class).getBody();
        } catch (Exception e) {
            logger.warn("getLocation(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }

}
