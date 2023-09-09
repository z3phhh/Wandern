package com.wandern.agent.metrics;

import com.wandern.agent.registration.RegisteredService;
import com.wandern.agent.registration.RegisteredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetricsCollectionService {

    private static final Logger logger = LoggerFactory.getLogger(MetricsCollectionService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final RestTemplate restTemplate;
    private final MetricsRepository metricsRepository;

    @Scheduled(fixedRate = 50000)  // every 50 sec
    public void collectMetricsFromRegisteredServices() {
        List<RegisteredService> services = registeredServiceRepository.findAll();

        for (RegisteredService service : services) {
            try {
                logger.info("Requesting metrics from service: {}", service.getServiceUrl());

                MetricsDTO metrics = restTemplate.getForObject(service.getServiceUrl() + "/metrics", MetricsDTO.class);

                logger.info("Received metrics from service {}: {}", service.getServiceUrl(), metrics);

                // Search for an existing metrics record for a given deploymentId
                Optional<MetricsEntity> existingMetrics = metricsRepository
                        .findByDeploymentId(service.getDeploymentId());

                MetricsEntity entity;
                if (existingMetrics.isPresent()) {
                    entity = existingMetrics.get();
                } else {
                    entity = new MetricsEntity();
                    entity.setDeploymentId(service.getDeploymentId());
                    entity.setSystem(service.getSystem());
                }

                entity.setSystemLoad(metrics != null ? metrics.systemLoad() : 0);
                entity.setJvmCpuLoad(metrics.jvmCpuLoad());
                entity.setUsedMemory(metrics.usedMemory());
                entity.setFreeMemory(metrics.freeMemory());
                entity.setTotalThreads(metrics.totalThreads());
                entity.setTimestamp(LocalDateTime.now());

                metricsRepository.save(entity);

            } catch (Exception e) {
                logger.error("Error while fetching metrics from service {}: {}",
                        service.getServiceUrl(),
                        e.getMessage());
            }
        }
    }
}

