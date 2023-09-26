package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatusDTO;
import com.wandern.starter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за проверку состояния зарегистрированных сервисов и отправку их статусов в мастер-сервис.
 */
@Component
@RequiredArgsConstructor
public class HealthCheckAgent {

    private final Logger logger = LoggerFactory.getLogger(HealthCheckAgent.class);
    private final Map<ServiceInfoDTO, ScheduledExecutorService> serviceSchedulers = new ConcurrentHashMap<>();
    private final Map<ServiceInfoDTO, Status> serviceStatuses = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${master.service.url}")
    private String masterServiceUrl;

    @Value("${healthcheck.interval}")
    private long healthCheckInterval;

    /**
     * Регистрирует сервис для регулярной проверки его состояния.
     * <p>
     * После регистрации сервис будет регулярно проверяться на доступность, и его статус будет отправляться в мастер-сервис.
     * </p>
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     */
    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            Status status = checkServiceHealth(serviceInfoDTO);
            serviceStatuses.put(serviceInfoDTO, status);
            sendStatusToMaster(serviceInfoDTO.deploymentId(), status);
        }, 0, healthCheckInterval, TimeUnit.SECONDS);
        serviceSchedulers.put(serviceInfoDTO, scheduler);
    }

    /**
     * Проверяет состояние указанного сервиса.
     * <p>
     * Метод пытается проверить состояние сервиса максимальное количество раз ({@code maxAttempts}), используя экспоненциальную задержку между попытками.
     * Если все попытки неудачны, возвращается статус {@link Status#DOWN}.
     * </p>
     *
     * @param service сервис, состояние которого нужно проверить.
     * @return статус сервиса.
     */
    /*
    private Status checkServiceHealth(ServiceInfoDTO service) {
    String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
    int maxAttempts = 100;
    int attempt = 1;
    int delay = 1000; // начальная задержка в миллисекундах (1 секунда)

    while (attempt <= maxAttempts) {
        try {
            ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
            Status status = Objects.requireNonNull(response.getBody()).status();
            if (status != Status.DOWN) {
                return status;
            }
        } catch (Exception e) {
            // + log
        }

        if (attempt < maxAttempts) {
            logger.warn("Attempt №{} to connect to the: {} failed. Retrying in {} seconds...", attempt, service.deploymentId(), delay / 1000);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                return Status.DOWN;
            }
            delay *= 2; // удваиваем задержку
        }
        attempt++;
    }

    logger.error("All attempts to check the health of service {} have failed.", service.deploymentId());
    return Status.DOWN;
}*/

    private Status checkServiceHealth(ServiceInfoDTO service) {
        String healthEndpoint = service.serviceUrl() + service.contextPath() + "/actuator/health";
        try {
            ResponseEntity<HealthStatus> response = restTemplate.getForEntity(healthEndpoint, HealthStatus.class);
            return Objects.requireNonNull(response.getBody()).status();
        } catch (Exception e) {
            logger.error("[HEALTH CHECK] Error checking service health for " + service.deploymentId(), e);
            return Status.DOWN;
        }
    }

    /**
     * Возвращает мапу статусов всех зарегистрированных сервисов.
     * <p>
     * Ключом карты является {@code deploymentId} сервиса, значением - его текущий статус.
     * </p>
     *
     * @return мапу статусов сервисов.
     */
    public Map<String, Status> getServiceStatuses() {
        return serviceStatuses.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().deploymentId(),
                        Map.Entry::getValue
                ));
    }

    /**
     * Отправляет статус указанного сервиса в мастер-сервис.
     *
     * @param deploymentId идентификатор развертывания сервиса.
     * @param status текущий статус сервиса.
     */
    public void sendStatusToMaster(String deploymentId, Status status) {
        String targetUrl = masterServiceUrl + "/master/api/v1/services/status/update";

        ServiceStatusDTO serviceStatus = new ServiceStatusDTO(deploymentId, status.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ServiceStatusDTO> request = new HttpEntity<>(serviceStatus, headers);

        try {
            restTemplate.postForEntity(targetUrl, request, Void.class);
        } catch (Exception e) {
            logger.error("Error sending status to master for deploymentId: " + deploymentId, e);
        }
    }
}