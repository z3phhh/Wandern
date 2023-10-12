package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatus;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class MasterMapper {

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
                .usedMemoryMB(DTO.usedMemoryMB())
                .freeMemoryMB(DTO.freeMemoryMB())
                .totalThreads(DTO.totalThreads())
                .build();
    }

    public static void updateMetricsFromDTO(MetricsDTO DTO, Metrics metrics) {
        metrics.setSystemLoad(DTO.systemLoad());
        metrics.setJvmCpuLoad(DTO.jvmCpuLoad());
        metrics.setUsedMemoryMB(DTO.usedMemoryMB());
        metrics.setFreeMemoryMB(DTO.freeMemoryMB());
        metrics.setTotalThreads(DTO.totalThreads());
    }

}