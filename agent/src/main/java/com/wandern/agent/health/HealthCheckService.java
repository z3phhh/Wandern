package com.wandern.agent.health;

import com.wandern.agent.AgentMapper;
import com.wandern.agent.AgentService;
import com.wandern.agent.data.LivenessDataStore;
import com.wandern.agent.data.ServiceInfoDataStore;
import com.wandern.clients.LivenessStatusDTO;
import com.wandern.model.ServiceInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthCheckService {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckService.class);

    private final WebClient webClient;
    private final ServiceInfoDataStore serviceInfoMap;
    private final LivenessDataStore livenessResults;
    private final AgentMapper agentMapper;

    @Scheduled(fixedRateString = "${liveness.probe.interval}") //fixed
    public void scheduledLivenessCheck() {
        serviceInfoMap.getAllServices()
                .forEach(this::checkLivenessProbe);
    }

    public void checkLivenessProbe(ServiceInfo serviceInfo) {
        String healthEndpoint = constructHealthEndpoint(serviceInfo);
        var status = new LivenessStatus();
        long startTime = System.nanoTime();

        webClient.get().uri(healthEndpoint)
                .retrieve()
                .toBodilessEntity()
                .doOnTerminate(() -> {
                    long duration = System.nanoTime() - startTime;
                    status.setLatencyMillis(duration / 1_000_000);  // convert to milliseconds
                    status.setTimestamp(LocalDateTime.now());
                    livenessResults.saveLivenessStatus(serviceInfo.deploymentId(), status);
                })
                .subscribe(
                        response -> {
                            if (response.getStatusCode() == HttpStatus.OK) {
                                status.setStatus("UP");
                                logger.info(healthEndpoint + " is UP");
                            } else {
                                status.setStatus("DOWN");
                                logger.warn(healthEndpoint + " has issues");
                            }
                        },
                        error -> {
                            status.setStatus("ERROR");
                            status.setError(error.getMessage());
                            logger.error("Error checking " + healthEndpoint, error);
                        }
                );
    }

    private String constructHealthEndpoint(ServiceInfo serviceInfo) {
        return serviceInfo.serviceUrl() + serviceInfo.contextPath() + "/actuator/health";
    }

    public LivenessStatusDTO getLivenessStatusDTO(String deploymentId) {
        var status = livenessResults.getLivenessStatus(deploymentId);
        return agentMapper.toDTO(status);
    }

    public Map<String, LivenessStatusDTO> getAllLivenessStatusesDTO() {
        return livenessResults.getAllLivenessStatuses()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> agentMapper.toDTO(entry.getValue())
                ));
    }

}