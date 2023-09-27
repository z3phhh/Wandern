package com.wandern.agent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/*
@Configuration
public class AgentConfig {

    @Value("${master.service.url}")
    private String masterServiceUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    @Bean
    public String masterServiceUrl() {
        return masterServiceUrl;
    }
    // TODO : возможно стоит удалить, поскольку в стартере есть
}*/
