package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.FilterDto;
import com.troojer.msevent.model.ProfileDto;
import com.troojer.msevent.model.exception.ClientException;
import com.troojer.msevent.model.exception.NotFoundException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProfileClient {
    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;

    @Value("${client.profile.url}")
    private String url;

    public ProfileClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public FilterDto getProfileFilter() {
        try {
            ResponseEntity<FilterDto> forEntity = restTemplate.getForEntity(url + "filter/", FilterDto.class);
            return forEntity.getBody();
        } catch (RestClientResponseException exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw switch (exc.getRawStatusCode()) {
                case 404 -> new NotFoundException("profile.profile.notFound");
                default -> new ClientException("default.service.temporaryError");
            };
        } catch (Exception exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw new ClientException("default.service.temporaryError");
        }
    }

    public ProfileDto getProfile(String userId) {
        try {
            ResponseEntity<ProfileDto> forEntity = restTemplate.getForEntity(url + userId, ProfileDto.class);
            return forEntity.getBody();
        } catch (RestClientResponseException exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw switch (exc.getRawStatusCode()) {
                case 404 -> new NotFoundException("profile.profile.notFound");
                default -> new ClientException("default.service.temporaryError");
            };
        } catch (Exception exc) {
            logger.warn("getFilter(); exc: ", exc);
            throw new ClientException("default.service.temporaryError");
        }
    }
}
