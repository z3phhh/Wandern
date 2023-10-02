package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatus;
import com.wandern.master.DTO.Metrics2DTO;
import com.wandern.master.DTO.ServiceDetailsDTO;
import com.wandern.master.DTO.projection.MetricsProjection;
import com.wandern.master.DTO.projection.ServiceDetailsProjection;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.repository.MetricsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class MasterMapper {

    private final MetricsRepository metricsRepository;

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

    public ServiceDetailsDTO convertToDTO(ServiceDetailsProjection projection) {
        MetricsProjection metricsProjection = projection.getMetrics();

        Metrics2DTO metrics2DTO = new Metrics2DTO(
                metricsProjection.getSystemLoad(),
                metricsProjection.getJvmCpuLoad(),
                metricsProjection.getUsedMemoryMB(),
                metricsProjection.getFreeMemoryMB(),
                metricsProjection.getTotalThreads()
        );

        return new ServiceDetailsDTO(
                projection.getDeploymentId(),
                projection.getDeploymentUnit(),
                projection.getSystem(),
                projection.getServiceUrl(),
                projection.getContextPath(),
                projection.getPort(),
                projection.getIp(),
                projection.getStatus().name(),
                metrics2DTO
        );
    }

}