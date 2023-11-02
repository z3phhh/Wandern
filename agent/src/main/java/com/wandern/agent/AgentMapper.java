package com.wandern.agent;

import com.wandern.agent.health.LivenessStatus;
import com.wandern.clients.LivenessStatusDTO;
import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.model.ServiceInfo;
import com.wandern.model.ServiceMetrics;
import org.springframework.stereotype.Component;

@Component
public class AgentMapper {

    public ServiceInfo fromDTO(ServiceInfoDTO DTO) {
        return ServiceInfo.builder()
                .deploymentId(DTO.deploymentId())
                .deploymentUnit(DTO.deploymentUnit())
                .system(DTO.system())
                .serviceUrl(DTO.serviceUrl())
                .contextPath(DTO.contextPath())
                .port(DTO.port())
                .ip(DTO.ip())
                .status(DTO.status())
                .programmingLanguage(DTO.programmingLanguage())
                .registrationTime(DTO.registrationTime())
                .build();
    }

    public ServiceInfoDTO toDTO(ServiceInfo info) {
        return ServiceInfoDTO.builder()
                .deploymentId(info.deploymentId())
                .deploymentUnit(info.deploymentUnit())
                .system(info.system())
                .serviceUrl(info.serviceUrl())
                .contextPath(info.contextPath())
                .port(info.port())
                .ip(info.ip())
                .status(info.status())
                .programmingLanguage(info.programmingLanguage())
                .registrationTime(info.registrationTime())
                .build();
    }

    public ServiceMetrics fromDTO(MetricsDTO DTO) {
        return ServiceMetrics.builder()
                .systemLoad(DTO.systemLoad())
                .jvmCpuLoad(DTO.jvmCpuLoad())
                .usedMemoryMB(DTO.usedMemoryMB())
                .freeMemoryMB(DTO.freeMemoryMB())
                .totalThreads(DTO.totalThreads())
                .build();
    }

    public MetricsDTO toDTO(ServiceMetrics metrics) {
        return MetricsDTO.builder()
                .systemLoad(metrics.systemLoad())
                .jvmCpuLoad(metrics.jvmCpuLoad())
                .usedMemoryMB(metrics.usedMemoryMB())
                .freeMemoryMB(metrics.freeMemoryMB())
                .totalThreads(metrics.totalThreads())
                .build();
    }

    public LivenessStatusDTO toDTO(LivenessStatus status) {
        return LivenessStatusDTO.builder()
                .status(status.getStatus())
                .timestamp(status.getTimestamp())
                .error(status.getError())
                .latencyMillis(status.getLatencyMillis())
                .build();
    }
}