package com.wandern.agent.health;

import com.wandern.starter.health.HealthCheckAggregator;
import com.wandern.starter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    private final HealthCheckAggregator healthCheckAggregator;
    private final HealthCheckAgent healthCheckAgent;

/*    @GetMapping
    public ResponseEntity<Map<String, HealthStatus>> getHealthStatus() {
        return ResponseEntity.ok(healthCheckAggregator.aggregateStatus());
    }*/

    @GetMapping("/services-status")
    public ResponseEntity<Map<String, Status>> getServicesStatus() {
        return ResponseEntity.ok(healthCheckAgent.getServiceStatuses());
    }

    // TODO : сделать расписание HC
}
