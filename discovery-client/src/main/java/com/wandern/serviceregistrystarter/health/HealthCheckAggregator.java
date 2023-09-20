//package com.wandern.serviceregistrystarter.health;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class HealthCheckAggregator {
//
//    private final List<HealthCheck> healthChecks;
//
//    public Map<String, HealthStatus> aggregateStatus() {
//        Map<String, HealthStatus> statusMap = new HashMap<>();
//        for (HealthCheck hc : healthChecks) {
//            statusMap.put(hc.getClass().getSimpleName(), hc.check());
//        }
//        return statusMap;
//    }
//}
