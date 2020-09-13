package com.troojer.mstag.configuration;

import org.slf4j.MDC;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestTemplateConfiguration {
    @Bean
    public RestTemplate getRestTemplate() {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(20));
        MDC.getCopyOfContextMap().forEach(restTemplateBuilder::defaultHeader);
        return restTemplateBuilder.build();
    }
}
