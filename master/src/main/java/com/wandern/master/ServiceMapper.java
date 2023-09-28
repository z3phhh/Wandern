package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatus;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;

import java.time.LocalDateTime;

public class ServiceMapper {

    public static RegisteredService toEntity(ServiceInfoDTO DTO) {
        return RegisteredService.builder()
                .deploymentId(DTO.deploymentId())
                .deploymentUnit(DTO.deploymentUnit())
                .system(DTO.system())
                .serviceUrl(DTO.serviceUrl())
                .contextPath(DTO.contextPath())
                .port(DTO.port())
                .ip(DTO.ip())
                .status(ServiceStatus.UP)
                .registrationTime(LocalDateTime.now())
                .build();
    }

    public static Metrics toEntity(MetricsDTO DTO) {
        return Metrics.builder()
                .systemLoad(DTO.systemLoad())
                .jvmCpuLoad(DTO.jvmCpuLoad())
                .usedMemoryBytes(DTO.usedMemoryBytes())
                .freeMemoryBytes(DTO.freeMemoryBytes())
                .totalThreads(DTO.totalThreads())
                .build();
    }

    public static void updateMetricsFromDTO(MetricsDTO DTO, Metrics metrics) {
        metrics.setSystemLoad(DTO.systemLoad());
        metrics.setJvmCpuLoad(DTO.jvmCpuLoad());
        metrics.setUsedMemoryBytes(DTO.usedMemoryBytes());
        metrics.setFreeMemoryBytes(DTO.freeMemoryBytes());
        metrics.setTotalThreads(DTO.totalThreads());
    }
}