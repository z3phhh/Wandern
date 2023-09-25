package com.wandern.agent;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервис, предоставляющий функциональность для работы с агентом.
 * Отвечает за регистрацию сервисов в агенте и мастере, а также за сбор и отправку метрик.
 */
@Service
@RequiredArgsConstructor
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);
    private final ScheduledExecutorService retryExecutor = Executors.newSingleThreadScheduledExecutor();
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private final Map<String, ScheduledFuture<?>> retryFutures = new ConcurrentHashMap<>();

    @Value("${master.service.url}")
    private String masterServiceUrl;

    private final RestTemplate restTemplate;
    private final ServiceRegistry serviceRegistry;

    public void registerServiceInAgent(ServiceInfoDTO serviceInfoDTO) {
        serviceRegistry.registerService(serviceInfoDTO);
        logger.info("Service with deploymentId '{}' registered successfully in agent.", serviceInfoDTO.deploymentId());
    }

    /**
     * Регистрирует сервис в мастере. В случае ошибки планирует повторную попытку (3 попытки с кд в 30 секунд).
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     */
    public void registerServiceInMaster(ServiceInfoDTO serviceInfoDTO) {
        try {
            restTemplate.postForEntity(masterServiceUrl + "/master/api/v1/services/register", serviceInfoDTO, Void.class);
            logger.info("Service with deploymentId '{}' registered successfully in master.", serviceInfoDTO.deploymentId());

            // Если регистрация прошла успешно, отменяем повторную попытку для этого deploymentId
            ScheduledFuture<?> future = retryFutures.remove(serviceInfoDTO.deploymentId());
            if (future != null) {
                future.cancel(false);
            }
            retryCount.set(0);  // Reset the retry count
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof ConnectException) {
                logger.warn("Master service is not available. Scheduling a retry for service with deploymentId '{}'.", serviceInfoDTO.deploymentId());
                scheduleRetry(serviceInfoDTO);
            } else {
                logger.error("Error accessing master service for service with deploymentId '{}'.", serviceInfoDTO.deploymentId(), e);
            }
        } catch (RestClientException e) {
            logger.error("Failed to register service with deploymentId '{}' in master.", serviceInfoDTO.deploymentId(), e);
        }
    }

    /**
     * Планирует повторную попытку регистрации сервиса в мастер-сервисе.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     */
    private void scheduleRetry(ServiceInfoDTO serviceInfoDTO) {
        ScheduledFuture<?> existingFuture = retryFutures.get(serviceInfoDTO.deploymentId());
        if (existingFuture == null || existingFuture.isDone()) {
            ScheduledFuture<?> future = retryExecutor.scheduleWithFixedDelay(() -> {
                int attempt = retryCount.incrementAndGet();
                logger.info("Retry attempt #{} to register in master for service with deploymentId '{}'.", attempt, serviceInfoDTO.deploymentId());
                try {
                    registerServiceInMaster(serviceInfoDTO);
                } catch (Exception ex) {
                    logger.error("Failed during retry registration in master for service with deploymentId '{}'. Next attempt in 1 minute.",
                            serviceInfoDTO.deploymentId(), ex);
                }
            }, 1, 1, TimeUnit.MINUTES);  // Повторная попытка каждую минуту
            retryFutures.put(serviceInfoDTO.deploymentId(), future);
        }
    }

    /**
     * Возвращает список всех зарегистрированных сервисов.
     *
     * @return коллекция зарегистрированных сервисов.
     */
    public Collection<ServiceInfoDTO> getAllServices() {
        return serviceRegistry.getAllServices();
    }

    /**
     * Собирает метрики от всех зарегистрированных сервисов и отправляет их в мастер-сервис.
     */
    @Scheduled(fixedRate = 10000)
    public void collectMetricsFromRegisteredServices() {
        Collection<ServiceInfoDTO> allServices = serviceRegistry.getAllServices();
//        logger.info("All registered services: {}", allServices);

        if (allServices.isEmpty()) {
            logger.info("No registered services found. Skipping metrics collection.");
            return;
        }

        for (ServiceInfoDTO service : allServices) {
            String metricsUrl = service.serviceUrl() + service.contextPath() + "/metrics";
//            logger.info("Requesting metrics from service: {}", metricsUrl);

            try {
                MetricsDTO metricsDTO = restTemplate.getForObject(metricsUrl, MetricsDTO.class);
                logger.info("Received metrics from service {}: {}", metricsUrl, metricsDTO);

                String metricsEndpoint = masterServiceUrl + "/master/api/v1/services/" + service.deploymentId() + "/metrics";
                logger.info("Sending metrics to master endpoint: {}", metricsEndpoint);

                ResponseEntity<String> response = restTemplate.postForEntity(metricsEndpoint, metricsDTO, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Metrics successfully sent to master for service: {}", metricsUrl);
                } else {
                    logger.error("Failed to send metrics to master for service: {}. Response: {}", metricsUrl, response);
                }
            } catch (ResourceAccessException e) {
                if (e.getCause() instanceof ConnectException) {
                    logger.warn("Service {} is not available or removed from load balancer. Skipping metrics collection for now.", metricsUrl);
                } else {
                    logger.error("Error accessing service {}.", metricsUrl, e);
                }
            } catch (RestClientException e) {
                logger.error("Failed to send metrics to master for service: {}.", metricsUrl, e);
            }
        }
    }
}
