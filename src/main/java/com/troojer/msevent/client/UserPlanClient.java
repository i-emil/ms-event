package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserPlanClient {

    private final Logger logger = (Logger)LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate;
    @Value("${client.user-plan.url}")
    private String url;

    public UserPlanClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getUserPlan() {
        try {
            return restTemplate.getForEntity(url, String.class).getBody();
        } catch (Exception e) {
            logger.warn("getUserPlan(); exc: ", e);
            return "BASIC";
        }
    }

}
