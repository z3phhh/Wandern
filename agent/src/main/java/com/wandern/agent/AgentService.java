package com.wandern.agent;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    @Value("${master.service.url}")
    private String masterServiceUrl;

    private final RestTemplate restTemplate;
    private final ServiceRegistry serviceRegistry;

    public void registerServiceInAgent(ServiceInfoDTO serviceInfoDTO) {
        serviceRegistry.registerService(serviceInfoDTO);
    }

    public void registerServiceInMaster(ServiceInfoDTO serviceInfoDTO) {
        try {
            restTemplate.postForEntity(masterServiceUrl + "/master/api/v1/services/register", serviceInfoDTO, Void.class);
            logger.info("Service registered successfully in master.");
        } catch (RestClientException e) {
            logger.error("Failed to register service in master.", e);
        }
    }

    public Collection<ServiceInfoDTO> getAllServices() {
        return serviceRegistry.getAllServices();
    }


    /**
     * Periodically collects metrics from all registered services.
     * Sends the collected metrics to the master.
     */
    @Scheduled(fixedRate = 10000)
    public void collectMetricsFromRegisteredServices() {
        Collection<ServiceInfoDTO> allServices = serviceRegistry.getAllServices();

        if (allServices.isEmpty()) {
            logger.info("No registered services found. Skipping metrics collection.");
            return;
        }

        for (ServiceInfoDTO service : allServices) {
            try {
                String metricsUrl = service.serviceUrl() + service.contextPath() + "/metrics";
                logger.info("Requesting metrics from service: {}", metricsUrl);

                MetricsDTO metricsDTO = restTemplate.getForObject(metricsUrl, MetricsDTO.class);
                logger.info("Received metrics from service {}: {}", metricsUrl, metricsDTO);

                String metricsEndpoint = masterServiceUrl + service.contextPath() + "/api/v1/services/metrics";
                ResponseEntity<String> response = restTemplate.postForEntity(metricsEndpoint, metricsDTO, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Metrics successfully sent to master for service: {}", metricsUrl);
                } else {
                    logger.error("Failed to send metrics to master for service: {}. Response: {}", metricsUrl, response);
                }

            } catch (Exception e) {
                logger.error("Error while fetching metrics from service {}: {}", service.serviceUrl() + service.contextPath(), e.getMessage());
            }
        }
    }
}
