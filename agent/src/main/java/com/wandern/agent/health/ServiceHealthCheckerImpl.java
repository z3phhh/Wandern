package com.wandern.agent.health;

import com.wandern.agent.AgentService;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.starter.health.HealthStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Component
public class ServiceHealthCheckerImpl implements ServiceHealthChecker {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public Status checkServiceHealth(ServiceInfoDTO service) {
        String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
        try {
            ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
            return Objects.requireNonNull(response.getBody()).status();
        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            logger.error("Failed to check health for service at {}. Response: {}", healthEndpoint, httpEx.getResponseBodyAsString(), httpEx);
        } catch (ResourceAccessException resourceEx) {
            logger.warn("Failed to access service at {}. Might be down or network issues.", healthEndpoint, resourceEx);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while checking health for service at {}", healthEndpoint, e);
        }
        return Status.DOWN;
    }
}