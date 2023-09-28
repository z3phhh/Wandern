package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.repository.MetricsRepository;
import com.wandern.master.repository.RegisteredServiceRepository;
import com.wandern.starter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.management.ServiceNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MasterService {

    private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final MetricsRepository metricsRepository;

    /**
     * Регистрирует сервис в глобальной топологии.
     * Преобразует DTO в сущность и сохраняет ее в базе данных.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return ответ о результате регистрации.
     */
    public ResponseEntity<String> registerService(ServiceInfoDTO serviceInfoDTO) {
        RegisteredService registeredService = ServiceMapper.toEntity(serviceInfoDTO);

        try {
            registeredServiceRepository.save(registeredService);
            logger.info("Service registered successfully with ID: {}", registeredService.getDeploymentId());
            return ResponseEntity.ok("Service registered successfully in master.");
        } catch (Exception e) {
            logger.error("Error registering service: {}", serviceInfoDTO, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to register service in master.");
        }
    }

    /**
     * Сохраняет метрики для указанного сервиса.
     * Поиск существующих метрик осуществляется по идентификатору развертывания.
     * Если метрики уже существуют, они обновляются. В противном случае создается новая запись.
     *
     * @param deploymentId идентификатор развертывания сервиса.
     * @param metricsDTO метрики сервиса.
     * @return ответ о результате сохранения метрик.
     */
    @Transactional
    public ResponseEntity<String> saveMetrics(String deploymentId, MetricsDTO metricsDTO) {
        RegisteredService registeredService = getRegisteredServiceOrThrow(deploymentId);

        Metrics metrics = metricsRepository.findByRegisteredService(registeredService)
                .map(existingMetrics -> {
                    ServiceMapper.updateMetricsFromDTO(metricsDTO, existingMetrics);
                    return existingMetrics;
                })
                .orElseGet(() -> {
                    Metrics newMetrics = ServiceMapper.toEntity(metricsDTO);
                    newMetrics.setRegisteredService(registeredService);
                    return newMetrics;
                });
        metrics.setTimestamp(LocalDateTime.now());

        try {
            metricsRepository.save(metrics);
            logger.info("Metrics saved successfully for deploymentId: {}", deploymentId);
            return ResponseEntity.ok("Metrics saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving metrics for deploymentId: {}", deploymentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving metrics for deploymentId: " + deploymentId);
        }
    }

    private RegisteredService getRegisteredServiceOrThrow(String deploymentId) {
        return registeredServiceRepository.findByDeploymentId(deploymentId)
                .orElseThrow(() -> {
                    logger.warn("No RegisteredService found for deploymentId: {}", deploymentId);
                    return new RuntimeException("RegisteredService not found for deploymentId: " + deploymentId);
                });
    }

    /**
     * Обновляет статус сервиса на основе предоставленного статуса здоровья.
     * Если статус "DOWN", сервис удаляется из базы данных.
     * В противном случае статус сервиса обновляется.
     *
     * @param deploymentId идентификатор развертывания сервиса.
     * @param healthStatus статус здоровья сервиса.
     * @return ответ о результате обновления статуса.
     */
    @Transactional
    public ResponseEntity<String> updateServiceStatus(String deploymentId,
                                                      HealthStatus healthStatus) {
        RegisteredService registeredService = getRegisteredServiceOrThrow(deploymentId);

        if (healthStatus.status() == Status.DOWN) {
            registeredServiceRepository.delete(registeredService);
            return ResponseEntity.ok("Service removed from master");
        } else {
            registeredServiceRepository.save(registeredService);
            return ResponseEntity.ok("Service status updated in master");
        }
    }

}