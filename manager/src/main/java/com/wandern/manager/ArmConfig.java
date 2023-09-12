package com.wandern.manager;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ArmConfig {

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

    @Value("${agent.service.url}")
    private String agentServiceUrl;

    @Bean
    public String agentServiceUrl() {
        return agentServiceUrl;
    }

    public String getAgentServiceUrl() {
        return agentServiceUrl;
    }

    public String getMasterServiceUrl() {
        return masterServiceUrl;
    }
}
