//package com.wandern.agent.health;
//
//import com.wandern.serviceregistrystarter.health.HealthCheckAggregator;
//import com.wandern.serviceregistrystarter.health.HealthStatus;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class HealthCheckScheduler {
//
//    @Value("${master.service.url}")
//    private String masterServiceUrl;
//
//    private final RestTemplate restTemplate;
//    private final HealthCheckAggregator healthCheckAggregator;
//
//    @Scheduled(fixedRate = 10000)
//    public void sendHealthStatusToMaster() {
//        Map<String, HealthStatus> healthStatusMap = healthCheckAggregator.aggregateStatus();
//        restTemplate.postForEntity(masterServiceUrl + "/api/health/status", healthStatusMap, Void.class);
//    }
//}
