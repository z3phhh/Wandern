package com.wandern.agent.data;

import com.wandern.model.ServiceInfo;
import com.wandern.model.ServiceMetrics;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceMetricsDataStore {

    private final Map<String, ServiceMetrics> serviceMetricsMap = new ConcurrentHashMap<>();

    public void saveServiceMetrics(String deploymentId, ServiceMetrics metrics) {
        serviceMetricsMap.put(deploymentId, metrics);
    }

    public ServiceMetrics getServiceMetrics(String deploymentId) {
        return serviceMetricsMap.get(deploymentId);
    }

    public Map<String, ServiceMetrics> getAllMetrics() {
        return Collections.unmodifiableMap(serviceMetricsMap);
    }

}
