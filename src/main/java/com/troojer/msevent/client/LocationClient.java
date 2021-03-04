package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.exception.ClientException;
import com.troojer.msevent.model.exception.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
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

    public void getLocation(String locationId) {
        try {
            restTemplate.getForEntity(url + locationId, Object.class).getBody();
        } catch (RestClientResponseException exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw switch (exc.getRawStatusCode()) {
                case 404 -> new NotFoundException("location not found");
                default -> new ClientException("default.service.temporaryError");
            };
        } catch (Exception e) {
            logger.warn("getLocation(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }

}
