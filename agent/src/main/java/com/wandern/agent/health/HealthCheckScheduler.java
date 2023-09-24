/*
package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.health.HealthCheckAggregator;
import com.wandern.serviceregistrystarter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HealthCheckScheduler {

    private final Logger logger = LoggerFactory.getLogger(HealthCheckScheduler.class);

    @Value("${master.service.url}")
    private String masterServiceUrl;

    private final RestTemplate restTemplate;
    private final HealthCheckAggregator healthCheckAggregator;

    @Scheduled(fixedRate = 10000)
    public void sendHealthStatusToMaster() {
        Map<String, HealthStatus> healthStatusMap = healthCheckAggregator.aggregateStatus();

        try {
            restTemplate.postForEntity(masterServiceUrl + "/master/api/v1/services/health/status", healthStatusMap, Void.class);
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof ConnectException) {
                logger.warn("Master service is not available. Failed to send aggregated health status.");
            } else {
                logger.error("Error sending aggregated health status to master.", e);
            }
        } catch (RestClientException e) {
            logger.error("Failed to send aggregated health status to master.", e);
        }
    }

    // проверить
}
*/
