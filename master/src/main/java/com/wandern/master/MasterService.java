package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
//import com.wandern.clients.ServiceStatus;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.repository.MetricsRepository;
import com.wandern.master.repository.RegisteredServiceRepository;
//import com.wandern.serviceregistrystarter.health.HealthStatus;
import com.wandern.serviceregistrystarter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

//import static com.wandern.clients.ServiceStatus.DOWN;
//import static com.wandern.clients.ServiceStatus.UP;

@Service
@RequiredArgsConstructor
public class MasterService {

    private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final MetricsRepository metricsRepository;
    private final ServiceMapper serviceMapper;

    public ResponseEntity<String> registerService(ServiceInfoDTO serviceInfoDTO) {
//        logger.info("Original ServiceInfoData: {}", serviceInfoDTO);
        RegisteredService registeredService = serviceMapper.toEntity(serviceInfoDTO);
//        logger.info("Mapped to RegisteredService entity: {}", registeredService);

        try {
            registeredServiceRepository.save(registeredService);
            logger.info("Service registered successfully with ID: {}", registeredService.getDeploymentId());
            return ResponseEntity.ok("Service registered successfully in master.");
        } catch (Exception e) {
            logger.error("Error registering service: {}", serviceInfoDTO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register service in master.");
        }
    }

    @Transactional
    public ResponseEntity<String> saveMetrics(String deploymentId, MetricsDTO metricsDTO) {
        logger.info("Attempting to find RegisteredService with deploymentId: {}", deploymentId);

        RegisteredService registeredService = registeredServiceRepository.findByDeploymentId(deploymentId)
                .orElseThrow(() -> {
                    logger.warn("No RegisteredService found for deploymentId: {}", deploymentId);
                    return new RuntimeException("RegisteredService not found");
                });

        Optional<Metrics> optionalExistingMetrics = metricsRepository.findByRegisteredService(registeredService);

        Metrics metrics;
        if (optionalExistingMetrics.isPresent()) {
            metrics = optionalExistingMetrics.get();
            serviceMapper.updateMetricsFromDTO(metricsDTO, metrics);
        } else {
            metrics = serviceMapper.toEntity(metricsDTO);
            metrics.setRegisteredService(registeredService);
        }

        metrics.setTimestamp(LocalDateTime.now());

        try {
            metricsRepository.save(metrics);
            logger.info("Metrics saved successfully for deploymentId: {}", deploymentId);
            return ResponseEntity.ok("Metrics saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving metrics for deploymentId: {}", deploymentId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving metrics.");
        }
    }

    public ResponseEntity<String> updateServiceStatus(String deploymentId, HealthStatus healthStatus) {
        RegisteredService registeredService = registeredServiceRepository.findByDeploymentId(deploymentId)
                .orElse(null);

        if (registeredService == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        if (healthStatus.status() == Status.DOWN) {
            // если сервис DOWN, удаляем его из базы данных или обновляем его статус (надо подумать)
            registeredServiceRepository.delete(registeredService);
            return ResponseEntity.ok("Service removed from master");
        } else {
            // если сервис UP, обновляем его статус или делаем другие мувы (надо подумать)
//             registeredService.setStatus(healthStatus.status());
             registeredServiceRepository.save(registeredService);
            return ResponseEntity.ok("Service status updated in master");
        }
    }


}