package com.wandern.agent;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AgentConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Value("${master.service.url}")
    private String masterServiceUrl;

    @Bean
    public String masterServiceUrl() {
        return masterServiceUrl;
    }

    public String getMasterServiceUrl() {
        return masterServiceUrl;
    }
}