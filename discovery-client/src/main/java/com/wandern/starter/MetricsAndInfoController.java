package com.wandern.starter;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Value("${agent.service.url}")
    private String agentServiceUrl;
    // вынести в конф

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        logger.info("Service registration triggered.");

        ServiceInfoDTO serviceInfoDTO = serviceInfoCollector.collectServiceInfo();

        String url = agentServiceUrl + "/ctx/api/v1/agent/register";
        int maxAttempts = 3;
        int currentAttempt = 0;

        while (currentAttempt < maxAttempts) {
            try {
                ResponseEntity<String> response = restTemplate.postForEntity(url, serviceInfoDTO, String.class);

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Service registered successfully at agent.");
                    return; // Если регистрация успешна, выходим из метода
                } else {
                    logger.error("Failed to register service at agent. Response: {}", response);
                }
            } catch (ResourceAccessException e) {
                logger.warn("Attempt {} of {}: Agent is not available. Retrying in 30 seconds...", currentAttempt + 1, maxAttempts);
                currentAttempt++;
                if (currentAttempt < maxAttempts) {
                    try {
                        Thread.sleep(30000); // Задержка в 30 секунд
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        logger.error("Sleep interrupted during retry delay.", ie);
                    }
                } else {
                    logger.error("Failed to register service at agent after {} attempts.", maxAttempts);
                }
            }
        }
    }


    @GetMapping("/metrics")
    public ResponseEntity<MetricsDTO> provideMetrics() {
        logger.info("[STARTER] Received request to provide metrics.");
        MetricsDTO metricsDTO = metricsCollector.collectMetrics();

//        logger.info("Returning metrics: {}", metricsDTO);
        return ResponseEntity.ok(metricsDTO);
    }
}