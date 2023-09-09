package com.wandern.serviceregistrystarter;

import com.wandern.serviceregistrystarter.metrics.MetricsDTO;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DTOMapper {

    public ServiceInfoDTO mapToServiceInfoDTO(ServiceInfo serviceInfo) {
        return new ServiceInfoDTO(
                serviceInfo.getDeploymentId(),
                serviceInfo.getSystem(),
                serviceInfo.getServiceUrl(),
                serviceInfo.getPort()
        );
    }

    public MetricsDTO mapToMetricsDTO(Map<String, Object> metricsMap, ServiceInfo serviceInfo) {
        return new MetricsDTO(
                (double) metricsMap.get("systemLoad"),
                (double) metricsMap.get("jvmCpuLoad"),
                (long) metricsMap.get("usedMemory"),
                (long) metricsMap.get("freeMemory"),
                (int) metricsMap.get("totalThreads"),
                serviceInfo.getDeploymentId(),
                serviceInfo.getSystem(),
                serviceInfo.getServiceUrl()
        );
    }
}
