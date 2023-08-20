package com.wandern.manager;

import com.wandern.agent.ServiceInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@Slf4j
@AllArgsConstructor
@Service
public class ServiceRegistration {

    private final RestTemplate restTemplate;
    private final Environment environment;

    @Value("${agent.service.url}")
    private String agentServiceUrl;

    @Autowired
    public ServiceRegistration(RestTemplate restTemplate, Environment environment) {
        this.restTemplate = restTemplate;
        this.environment = environment;
    }

    @PostConstruct
    public void register() {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setSystem("Manager");
        serviceInfo.setDeploymentId("v1.0.0");
        serviceInfo.setAddress("localhost");
        serviceInfo.setPort(Integer.parseInt(environment.getProperty("server.port")));
        serviceInfo.setHealthEndpoint("/health");

        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity(agentServiceUrl + "/register", serviceInfo, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("TestService registered successfully");
            } else {
                log.warn("TestService registration failed, status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error registering TestService", e);
        }
    }
}
