package com.wandern.agent;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class AgentService {

    private final RestTemplate restTemplate;
    private final String masterServiceUrl;
    private final Map<String, ServiceInfo> registeredServices = new ConcurrentHashMap<>();
    private final Map<String, ServiceInfo> localTopology = new ConcurrentHashMap<>();
    private final Set<String> warnedServices = new HashSet<>();


    public ResponseEntity<String> registerService(ServiceInfo serviceInfo) {
        String serviceKey = serviceInfo.getSystem() + "-" + serviceInfo.getDeploymentId();
        serviceInfo.setBalancingEnabled(true);
        registeredServices.put(serviceKey, serviceInfo);
        log.info("Registered instance {}", serviceInfo);

        sendServiceInfoToMaster(serviceInfo);

        return ResponseEntity.ok("Service registered");
    }

    private void sendServiceInfoToMaster(ServiceInfo serviceInfo) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(masterServiceUrl + "/register", serviceInfo, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Service registered successfully in master");
            } else {
                log.warn("Service registration in master failed, status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error registering service in master", e);
        }
    }

    @Scheduled(fixedRate = 3000) // каждые 3 секунды
    public void checkServicesHealth() {
        Map<String, Boolean> healthStatusUpdates = new HashMap<>();
        for (ServiceInfo serviceInfo : localTopology.values()) {
            String serviceKey = serviceInfo.getSystem() + "-" + serviceInfo.getDeploymentId();
            if (!serviceInfo.isBalancingEnabled()) {
                if (!warnedServices.contains(serviceKey)) {
                    log.info("Skipping health check for service {} as it has been manually removed from balancing", serviceKey);
                    warnedServices.add(serviceKey);
                }
                continue;
            } else {
                warnedServices.remove(serviceKey);
            }
            String url =
                    "http://"
                            + serviceInfo.getAddress()
                            + ":"
                            + serviceInfo.getPort()
                            + serviceInfo.getHealthEndpoint();
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Service {} is healthy", serviceKey);
                    healthStatusUpdates.put(serviceKey, true);
                } else {
                    log.warn("Service {} is not healthy, status code: {}", serviceKey, response.getStatusCode());
                    healthStatusUpdates.put(serviceKey, false);
                }
            } catch (ResourceAccessException e) {
                log.error("Error checking health of service {}: Connection refused", serviceKey);
                healthStatusUpdates.put(serviceKey, false);
            } catch (Exception e) {
                log.error("Error checking health of service {}", serviceKey, e);
                healthStatusUpdates.put(serviceKey, false);
            }
        }
        sendHealthStatusToMaster(healthStatusUpdates);
    }

    @Scheduled(fixedRateString = "${topology.update.interval}")
    public void updateLocalTopology() {
        try {
            ResponseEntity<Map> response = restTemplate.getForEntity(masterServiceUrl + "/getTopology", Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                localTopology.clear();
                Map<String, LinkedHashMap> responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                for (Map.Entry<String, LinkedHashMap> entry : responseBody.entrySet()) {
                    ServiceInfo serviceInfo = objectMapper.convertValue(entry.getValue(), ServiceInfo.class);
                    localTopology.put(entry.getKey(), serviceInfo);
                    if (!serviceInfo.isBalancingEnabled() && !warnedServices.contains(entry.getKey())) {
                        log.warn("Service {} has been manually removed from balancing",
                                serviceInfo.getSystem() + "-" + serviceInfo.getDeploymentId());
                        warnedServices.add(entry.getKey());
                    }
                }
                log.info("Local topology updated successfully");
            } else {
                log.warn("Failed to update local topology, status code: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error updating local topology", e);
        }
    }

    public Map<String, ServiceInfo> getLocalTopology() {
        return localTopology;
    }

    private void sendHealthStatusToMaster(Map<String, Boolean> healthStatusUpdates) {
        try {
            restTemplate.postForEntity(masterServiceUrl + "/updateHealthStatus",
                    healthStatusUpdates, String.class);
        } catch (Exception e) {
            log.error("Error sending health status to master", e);
        }
    }

}
