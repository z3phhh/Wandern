package com.wandern.agent;

import com.wandern.agent.data.ServiceInfoDataStore;
import com.wandern.agent.data.ServiceMetricsDataStore;
import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.model.ServiceInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Сервис, предоставляющий функциональность для работы с агентом.
 * Отвечает за регистрацию сервисов в агенте и мастере, а также за сбор и отправку метрик.
 */
@Service
@RequiredArgsConstructor
public class AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);

    @Value("${master.url}")
    private String masterServiceUrl;

    @Value("${master.registration.retry.interval}")
    private Long retryInterval;

    private final WebClient webClient;
    private final ServiceInfoDataStore serviceInfoMap;
    private final ServiceMetricsDataStore serviceMetricsMap;
    private final AgentMapper agentMapper;

    private final KafkaTemplate<String, MetricsDTO> kafkaTemplate;

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        var serviceInfo = agentMapper.fromDTO(serviceInfoDTO);
        serviceInfoMap.saveServiceInfo(serviceInfo.deploymentId(), serviceInfo); // save in agent memory

        registerInMaster(serviceInfo); // send to master bd
    }

    private void registerInMaster(ServiceInfo serviceInfo) {
        webClient.post()
                .uri(masterServiceUrl + "/master/api/v1/services/register")
                .body(BodyInserters.fromValue(serviceInfo))
                .retrieve()
                .bodyToMono(Void.class)
                .doOnSuccess(response ->
                        logger.info("Successfully registered service with deploymentId: {}", serviceInfo.deploymentId()))
                .doOnError(error ->
                        logger.error("Failed to register service with deploymentId: {}. Error: {}",
                                serviceInfo.deploymentId(), error.getMessage()))
                .onErrorResume(error ->
                        Mono.delay(Duration.ofMillis(retryInterval)).then(Mono.fromRunnable(() ->
                                registerInMaster(serviceInfo))))
                .subscribe();
    }


    public List<ServiceInfoDTO> getAllServices() {
        return serviceInfoMap.getAllServices().stream()
                .map(agentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Map<String, MetricsDTO> getAllServiceMetrics() {
        return serviceMetricsMap.getAllMetrics().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> agentMapper.toDTO(entry.getValue())
                ));
    }

    @Scheduled(fixedDelay = 10000)
    public void collectAndSendMetrics() {
        logger.info("Starting the metrics collection process...");

        serviceInfoMap.getAllServices()
                .forEach(serviceInfo -> fetchMetrics(serviceInfo)
                        .doOnNext(fetchedMetrics -> {
                            var metrics = agentMapper.fromDTO(fetchedMetrics);
                            serviceMetricsMap.saveServiceMetrics(serviceInfo.deploymentId(), metrics);
                            logger.info("Metrics collected and saved for service: {}", serviceInfo.deploymentId());

                            // Sending to Kafka
                            kafkaTemplate.send("metrics-topic", fetchedMetrics);
                            logger.info("Metrics sent to Kafka for service: {}", serviceInfo.deploymentId());
                        })
                        .doOnError(error -> logger.error("Error occurred while processing metrics for service: {}",
                                serviceInfo.deploymentId(), error))
                        .subscribe());

        logger.info("Metrics collection process completed.");
    }

    private Mono<MetricsDTO> fetchMetrics(ServiceInfo serviceInfo) {
        var metricsEndpoint = serviceInfo.serviceUrl() + serviceInfo.contextPath() + "/metrics";

        logger.info("Starting to fetch metrics for service with deployment ID: {}. URL: {}",
                serviceInfo.deploymentId(), metricsEndpoint);

        return webClient.get()
                .uri(metricsEndpoint)
                .retrieve()
                .bodyToMono(MetricsDTO.class)
                .doOnNext(dto -> logger.info("Successfully fetched metrics deployment ID: {}", serviceInfo.deploymentId()))
                .doOnError(e -> logger.error("Error while fetching metrics deployment ID: {}", serviceInfo.deploymentId(), e))
                .onErrorResume(e -> {
                    logger.warn("Resuming after error by returning an empty Mono for deployment ID: {}", serviceInfo.deploymentId());
                    return Mono.empty();
                });
    }

///////////
/*    public void registerServiceInAgent(ServiceInfoDTO serviceInfoDTO) {
        serviceRegistry.registerService(serviceInfoDTO);
        if (logger.isInfoEnabled()) {
            logger.info("Service with deploymentId '{}' registered successfully in agent.", serviceInfoDTO.deploymentId());
        }
    }*/

    /**
     * Пытается зарегистрировать указанный сервис в мастер-сервисе.
     * При успешной регистрации отменяет любые запланированные повторные попытки регистрации для данного deploymentId.
     * Если возникает проблема с подключением, планируется повторная попытка.
     *
     * @param serviceInfoDTO Информация о сервисе для регистрации.
     * @see #scheduleRetry(ServiceInfoDTO) Метод для планирования повторной попытки регистрации.
     */
/*    public void registerServiceInMaster(ServiceInfoDTO serviceInfoDTO) {
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

    *//**
     * Планирует повторную попытку регистрации указанного сервиса в мастер-сервисе.
     * Повторная попытка планируется с фиксированным интервалом, который можно настроить в конфигурационном файле.
     *
     * @param serviceInfoDTO Информация о сервисе для которого планируется повторная попытка регистрации.
     * @see #registerServiceInMaster(ServiceInfoDTO) Основной метод регистрации сервиса.
     *//*
    @Value("${master.registration.retry.interval}")
    private long retryInterval;
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
            }, retryInterval, retryInterval, TimeUnit.SECONDS);
            retryFutures.put(serviceInfoDTO.deploymentId(), future);
        }
    }

    *//**
     * Собирает метрики от всех зарегистрированных сервисов и отправляет их в мастер-сервис.
     *//*

    @Value("${metrics.collection.interval.seconds}")
    private long metricsCollectionIntervalSeconds;
    @Scheduled(fixedRateString = "#{${metrics.collection.interval.seconds} * 1000}")
    public void collectMetricsFromRegisteredServices() {
        Collection<ServiceInfoDTO> allServices = serviceRegistry.getAllServices();
        logger.info("All registered services: {}", allServices);

        if (allServices.isEmpty()) {
            logger.info("No registered services found. Skipping metrics collection.");
            return;
        }

        for (ServiceInfoDTO service : allServices) {
            String metricsUrl = service.serviceUrl() + service.contextPath() + "/metrics";
            logger.info("Requesting metrics from service: {}", metricsUrl);

            try {
                var metricsDTO = restTemplate.getForObject(metricsUrl, MetricsDTO.class);
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
    }*/

}
