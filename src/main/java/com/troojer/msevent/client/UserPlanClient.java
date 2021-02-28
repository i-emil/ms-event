package com.troojer.msevent.client;

import ch.qos.logback.classic.Logger;
import com.troojer.msevent.model.UserPlanDto;
import com.troojer.msevent.model.exception.ClientException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

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
            return restTemplate.getForEntity(url + "current", UserPlanDto.class).getBody().getUserPlan();
        } catch (Exception e) {
            logger.warn("getUserPlan(); exc: ", e);
            return "BASIC";
        }
    }

    @Cacheable("plansPermits")
    public Map<String, Map<String, Integer>> getPlansPermits() {
        try {
            ParameterizedTypeReference<Map<String, Map<String, Integer>>> responseType = new ParameterizedTypeReference<>() {
            };
            return restTemplate.exchange(url + "info/packs", GET, HttpEntity.EMPTY, responseType).getBody();
        } catch (Exception e) {
            logger.warn("getUserPlan(); exc: ", e);
            throw new ClientException(e.getMessage());
        }
    }

    public Integer getPermitValue(String userPlan, String permit) {
        return getPlansPermits().get(userPlan).get(permit);
    }

}
