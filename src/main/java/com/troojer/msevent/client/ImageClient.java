package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ImageClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());

    private final RestTemplate restTemplate;
    private final String bucket = "event-cover";
    @Value("${client.image.url}")
    private String url;

    public ImageClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getImageUrl(String imageId) {
        try {
            return restTemplate.getForEntity(url + "url/" + bucket + "/" + imageId, String.class).getBody();
        } catch (Exception exc) {
            logger.warn("getImageUrl(); exc: ", exc);
            throw new ClientException(exc.getMessage());
        }
    }

    public void deleteImage(String imageId) {
        try {
            restTemplate.exchange(url + bucket + "/" + imageId, HttpMethod.DELETE, null, Void.class);
            logger.info("deleteImage(): {}", imageId);
        } catch (Exception exc) {
            logger.warn("deleteImage(); exc: ", exc);
        }
    }

    public Boolean isImageExist(String imageId) {
        try {
            return restTemplate.getForEntity(url+"check/" + bucket + "/" + imageId, Boolean.class).getBody();
        } catch (Exception exc) {
            logger.warn("isImageExist(); exc: ", exc);
            throw new ClientException(exc.getMessage());
        }
    }
}
