package com.wandern.master;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterService {

    // Возможно, у вас есть репозиторий для сохранения ServiceInfo в базе данных
    private final ServiceInfoRepository serviceInfoRepository;
    private final MetricsRepository metricsRepository;

    public void registerService(ServiceInfoDTO serviceInfoDto) {
        ServiceInfo serviceInfo = convertDtoToEntity(serviceInfoDto);
        serviceInfoRepository.save(serviceInfo);
    }

    public void collectMetrics(MetricsDTO metricsDto) {
        Metrics metrics = convertDtoToMetricsEntity(metricsDto);
        metricsRepository.save(metrics);
    }

    private ServiceInfo convertDtoToEntity(ServiceInfoDTO dto) {
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setDeploymentId(dto.deploymentId());
        serviceInfo.setSystem(dto.system());
        serviceInfo.setServiceUrl(dto.serviceUrl());
        serviceInfo.setPort(dto.port());
        // Заполните другие поля, если они есть
        return serviceInfo;
    }

    private Metrics convertDtoToMetricsEntity(MetricsDTO dto) {
        Metrics metrics = new Metrics();
        metrics.setSystemLoad(dto.systemLoad());
        metrics.setJvmCpuLoad(dto.jvmCpuLoad());
        metrics.setUsedMemory(dto.usedMemory());
        metrics.setFreeMemory(dto.freeMemory());
        metrics.setTotalThreads(dto.totalThreads());
        // Заполните другие поля, если они есть
        return metrics;
    }
}
