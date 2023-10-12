package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HealthCheckAgent {

    private final Map<ServiceInfoDTO, ScheduledExecutorService> serviceSchedulers = new ConcurrentHashMap<>();
    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();
    private final ServiceHealthChecker serviceHealthChecker;
    private final ServiceStatusReporter serviceStatusReporter;

    @Value("${healthcheck.interval}")
    private long healthCheckInterval;

    public void startMonitoringService(ServiceInfoDTO serviceInfoDTO) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            var status = serviceHealthChecker.checkServiceHealth(serviceInfoDTO);
            serviceStatuses.put(serviceInfoDTO, status);
            serviceStatusReporter.sendStatusToMaster(serviceInfoDTO.deploymentId(), status);
        }, 0, healthCheckInterval, TimeUnit.SECONDS);
        serviceSchedulers.put(serviceInfoDTO, scheduler);
    }

    public Map<String, Status> getServiceStatuses() {
        return serviceStatuses.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().deploymentId(),
                        Map.Entry::getValue
                ));
    }
}