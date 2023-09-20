//package com.wandern.agent.health;
//
//import com.wandern.serviceregistrystarter.health.HealthCheckAggregator;
//import com.wandern.serviceregistrystarter.health.HealthStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/health")
//public class HealthCheckController {
//
//    private final HealthCheckAggregator healthCheckAggregator;
//
//    @GetMapping
//    public ResponseEntity<Map<String, HealthStatus>> getHealthStatus() {
//        return ResponseEntity.ok(healthCheckAggregator.aggregateStatus());
//    }
//}