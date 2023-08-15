package com.serviceAgent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@AllArgsConstructor
@RestController
public class AgentController {

    private final Map<String, ServiceInfo> registeredServices = new ConcurrentHashMap<>();

    private RestTemplate restTemplate;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfo serviceInfo) {
        registeredServices.put(serviceInfo.getName(), serviceInfo);
        log.info("Registered instance {}", serviceInfo);
        return ResponseEntity.ok("Service registered");
    }

    @Scheduled(fixedRate = 3000) // каждые 3 секунды
    public void checkServicesHealth() {
        for (ServiceInfo serviceInfo : registeredServices.values()) {
            String url =
                    "http://"
                    + serviceInfo.getAddress()
                    + ":"
                    + serviceInfo.getPort()
                    + serviceInfo.getHealthEndpoint();
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
                if (response.getStatusCode().is2xxSuccessful()) {
                    log.info("Service {} is healthy", serviceInfo.getName());
                } else {
                    log.warn("Service {} is not healthy, status code: {}", serviceInfo.getName(), response.getStatusCode());
                }
            } catch (Exception e) {
                log.error("Error checking health of service {}", serviceInfo.getName(), e);
            }
        }
    }
}