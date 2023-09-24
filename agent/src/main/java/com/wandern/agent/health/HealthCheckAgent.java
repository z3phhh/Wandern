package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
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

    private final Map<ServiceInfoDTO, ScheduledExecutorService> serviceSchedulers = new ConcurrentHashMap<>();
//    private final HealthCheckService healthCheckService;
    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final HealthStatusSender healthStatusSender;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10); // предположим, что у нас может быть до 10 сервисов

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Status status = checkServiceHealth(serviceInfoDTO);
            serviceStatuses.put(serviceInfoDTO, status);
            healthStatusSender.sendStatusToMaster(serviceInfoDTO.deploymentId(), status);
        }, 0, 30, TimeUnit.SECONDS);
        serviceSchedulers.put(serviceInfoDTO, scheduler);
    }

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