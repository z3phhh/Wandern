package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.starter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private final Logger logger = LoggerFactory.getLogger(HealthCheckAgent.class);
    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();


    private final List<ServiceInfoDTO> registeredServices;
    private final RestTemplate restTemplate;

    @Value("${master.service.url}")
    private String masterServiceUrl;

/*    public void checkServiceHealth(ServiceInfoDTO service) {
        String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
        try {
            ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
            Status currentStatus = Objects.requireNonNull(response.getBody()).status();
            serviceStatuses.put(service, currentStatus);
            if (currentStatus == Status.DOWN) {
                logger.warn("Service {} reported DOWN status", service.deploymentId());
                handleServiceDown(service);
            }
        } catch (Exception e) {
            logger.error("Error checking health for service {}: {}", service.deploymentId(), e.getMessage());
            handleServiceDown(service);
        }
    }


    private void handleServiceDown(ServiceInfoDTO service) {
        logger.warn("Handling DOWN service {}", service.deploymentId());
        int maxAttempts = 100;
        long delay = 2000; // начальная задержка в 2 секунды

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                Thread.sleep(delay);
                String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
                ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
                if (Objects.requireNonNull(response.getBody()).status() == Status.UP) {
                    logger.info("Service {} is now UP", service.deploymentId());
                    return;
                }
            } catch (Exception e) {
                logger.error("Error during retry for service {}: {}", service.deploymentId(), e.getMessage());
            }
            logger.warn("Attempt {} failed. Next attempt in {} seconds.", attempt, delay / 1000);
            delay *= 2; // удваиваем задержку
        }
        logger.error("Service {} is DOWN after retries", service.deploymentId());
        sendStatusToMaster(service, Status.DOWN);
    }*/


/*    private void sendStatusToMaster(ServiceInfoDTO service, Status status) {
        String targetUrl = masterServiceUrl + "/master/api/v1/services/health/status/" + service.deploymentId();
        HealthStatus healthStatus = new HealthStatus(status, "Service status update");

        try {
            restTemplate.postForEntity(targetUrl, healthStatus, Void.class);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof ConnectException) {
                logger.warn("Master service is not available. Failed to send status update for service '{}'.", service.deploymentId());
            } else {
                logger.error("Error sending status update to master for service '{}'.", service.deploymentId(), e);
            }
        } catch (RestClientException e) {
            logger.error("Failed to send status update to master for service '{}'.", service.deploymentId(), e);
        }
    }*/

}

