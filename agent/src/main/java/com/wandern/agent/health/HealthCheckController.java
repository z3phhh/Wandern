package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.health.HealthCheckAggregator;
import com.wandern.serviceregistrystarter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    private final HealthCheckAggregator healthCheckAggregator;
    private final HealthCheckAgent healthCheckAgent;
//    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();


    @GetMapping
    public ResponseEntity<Map<String, HealthStatus>> getHealthStatus() {
        return ResponseEntity.ok(healthCheckAggregator.aggregateStatus());
    }

/*    @GetMapping("/schedulers-status")
    public ResponseEntity<Map<String, Boolean>> getSchedulersStatus() {
        return ResponseEntity.ok(healthCheckAgent.getSchedulersStatus());
    }

    @GetMapping("/tracked-services")
    public ResponseEntity<Map<String, Boolean>> getTrackedServicesStatus() {
        return ResponseEntity.ok(healthCheckAgent.getSchedulersStatus());
    }*/

    // working!
    @GetMapping("/services-status")
    public ResponseEntity<Map<String, Status>> getServicesStatus() {
        return ResponseEntity.ok(healthCheckAgent.getServiceStatuses());
    }
}
