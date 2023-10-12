package com.wandern.starter;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
public class MetricsAndInfoController {

    private static final Logger logger = LoggerFactory.getLogger(MetricsAndInfoController.class);

    private final RestTemplate restTemplate;
    private final ServiceInfoCollector serviceInfoCollector;
    private final MetricsCollector metricsCollector;
    private final ApplicationContext appContext;

    @Value("${agent.url}")
    private String agentServiceUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        logger.info("Service registration triggered.");

        var serviceInfoDTO = serviceInfoCollector.collectServiceInfo();

        String url = agentServiceUrl + "/ctx/api/v1/agent/register";
        var currentAttempt = 0;

        for (;;) {
            currentAttempt++;
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, serviceInfoDTO, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Service registered successfully at agent on attempt {}.", currentAttempt);
                    return; // Если регистрация успешна, выходим из метода
                } else {
                    logger.error("Failed to register service at agent on attempt {}. Response: {}", currentAttempt, response);
                }
            } catch (ResourceAccessException e) {
                logger.warn("Attempt {}: Agent is not available. Retrying in 30 seconds...", currentAttempt);
                try {
                    Thread.sleep(30000); // Задержка в 30 секунд
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    logger.error("Sleep interrupted during retry delay.", ie);
                }
            }
        }
    }

    @GetMapping("/metrics")
    public ResponseEntity<MetricsDTO> provideMetrics() {
        logger.info("Received request to provide metrics.");
        var metricsDTO = metricsCollector.collectMetrics();

        return ResponseEntity.ok(metricsDTO);
    }

    @PostMapping("/shutdown")
    public String shutdownApplication() {
        SpringApplication.exit(appContext, () -> 0);
        return "Application is shutting down...";
    }
}