package com.wandern.agent.registration;

import com.wandern.agent.metrics.MetricsRepository;
import com.wandern.clients.agent.ServiceInfoResponce;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ServiceRegistrationService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRegistrationService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final RestTemplate restTemplate;
//    private final MetricsRepository metricsRepository;

    @Value("${master.service.url}")
    private String masterServiceUrl;

//    public void registerService(ServiceInfoDTO serviceInfoDto) {
//        RegisteredService service = new RegisteredService();
//        service.setDeploymentId(serviceInfoDto.deploymentId());
//        service.setServiceUrl(serviceInfoDto.serviceUrl());
//        registeredServiceRepository.save(service);
//    }

//    public void saveMetrics(MetricsDTO metricsDto, String deploymentId, String system, String serviceUrl) {
//        MetricsEntity entity = new MetricsEntity();
//        entity.setDeploymentId(deploymentId);
//        entity.setSystem(system);
//        entity.setServiceUrl(serviceUrl);
//        entity.setSystemLoad(metricsDto.systemLoad());
//        entity.setJvmCpuLoad(metricsDto.jvmCpuLoad());
//        entity.setUsedMemory(metricsDto.usedMemory());
//        entity.setFreeMemory(metricsDto.freeMemory());
//        entity.setTotalThreads(metricsDto.totalThreads());
//        entity.setTimestamp(LocalDateTime.now());
//        metricsRepository.save(entity);
//    }

//    @Scheduled(fixedRate = 30000)  // poll every 30 sec
//    public void pollRegisteredServices() {
//        List<RegisteredService> services = registeredServiceRepository.findAll();
//
//        for (RegisteredService service : services) {
//            try {
//                logger.info("Requesting metrics from service: {}", service.getSystem() + " - " + service.getServiceUrl());
//
//                MetricsDTO metrics = restTemplate.getForObject(service.getServiceUrl() + "/metrics", MetricsDTO.class);
//
//                logger.info("Received metrics from service {}: {}", service.getSystem() + " - " + service.getServiceUrl(), metrics);
//
//                // Metrics processing, e.g. saving to a database (I'll get to it later.)
//                service.setLastUpdate(LocalDateTime.now());
//                registeredServiceRepository.save(service);
//            } catch (Exception e) {
//                logger.error("Error while fetching metrics from service {}: {}", service.getServiceUrl(), e.getMessage());
//            }
//        }
//    }

    public void registerServiceInAgentDB(ServiceInfoResponce serviceInfoResponce) {
        RegisteredService registeredService = new RegisteredService();
        registeredService.setDeploymentId(serviceInfoResponce.deploymentId());
        registeredService.setSystem(serviceInfoResponce.system());
        registeredService.setServiceUrl(serviceInfoResponce.serviceUrl());
        registeredService.setPort(serviceInfoResponce.port());
        // Additional fields can be set here
        registeredServiceRepository.save(registeredService);
    }
    public void registerServiceWithMaster(ServiceInfoResponce serviceInfoResponce) {
        try {
            restTemplate.postForEntity(masterServiceUrl + "/api/services/register", serviceInfoResponce, Void.class);
        } catch (RestClientException e) {
            // check
            logger.warn("Failed to register with master. Maybe master is not running?", e);
        }
    }


    // a little later
//    public void sendMetricsToMaster(MetricsDTO metricsDto) {
//        // logic
//        restTemplate.postForEntity(masterServiceUrl + "/api/metrics/collect", metricsDto, Void.class);
//    }
}
