package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.starter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HealthCheckAgent {

    private final Logger logger = LoggerFactory.getLogger(HealthCheckAgent.class);
    private final Map<ServiceInfoDTO, ScheduledExecutorService> serviceSchedulers = new ConcurrentHashMap<>();
    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final HealthStatusSender healthStatusSender;

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Status status = checkServiceHealth(serviceInfoDTO);
            serviceStatuses.put(serviceInfoDTO, status);
            healthStatusSender.sendStatusToMaster(serviceInfoDTO.deploymentId(), status);
        }, 0, 30, TimeUnit.SECONDS);
        serviceSchedulers.put(serviceInfoDTO, scheduler);
    }

/*    private Status checkServiceHealth(ServiceInfoDTO service) {
        String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
        int maxAttempts = 100;
        int attempt = 1;
        int delay = 1000; // начальная задержка в миллисекундах (1 секунда)

        while (attempt <= maxAttempts) {
            try {
                ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
                Status status = Objects.requireNonNull(response.getBody()).status();
                if (status != Status.DOWN) {
                    return status;
                }
            } catch (Exception e) {
                // + log
            }

            if (attempt < maxAttempts) {
                logger.warn("Attempt {} failed. Retrying in {} seconds...", attempt, delay / 1000);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return Status.DOWN;
                }
                delay *= 2; // удваиваем задержку
            }
            attempt++;
        }

        logger.error("All attempts to check the health of service {} have failed.", service.deploymentId());
        return Status.DOWN;
    }*/

    private Status checkServiceHealth(ServiceInfoDTO service) {
        String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
        try {
            ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
            return Objects.requireNonNull(response.getBody()).status();
        } catch (Exception e) {
            return Status.DOWN;
        }
    }

    public Map<String, Status> getServiceStatuses() {
        return serviceStatuses.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().deploymentId(),
                        Map.Entry::getValue
                ));
    }
}