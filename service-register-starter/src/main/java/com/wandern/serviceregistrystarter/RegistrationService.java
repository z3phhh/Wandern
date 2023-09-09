package com.wandern.serviceregistrystarter;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class RegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);

    private final RestTemplate restTemplate;
    private final ServiceInfo serviceInfo;
    private final DTOMapper dtoMapper;

    @Value("${agent.service.url}")
    private String agentServiceUrl;

    public void registerAtStartup() {
        logger.info("Registering service at startup...");

        ServiceInfoDTO dto = dtoMapper.mapToServiceInfoDTO(serviceInfo);

        String url = agentServiceUrl + "/agent/register";
        ResponseEntity<String> response = restTemplate.postForEntity(url, dto, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            logger.info("Service registered successfully at agent.");
        } else {
            logger.error("Failed to register service at agent. Response: {}", response);
        }
    }
}


