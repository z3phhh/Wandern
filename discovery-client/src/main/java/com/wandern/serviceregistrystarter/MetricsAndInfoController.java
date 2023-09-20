package com.wandern.serviceregistrystarter;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.dto.ServiceInfoDTOResponce;
import com.wandern.serviceregistrystarter.dto.MetricsDTOResponce;
import com.wandern.serviceregistrystarter.model.MetricsData;
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

// TODO: в контроллере вывести логику
@RestController
//@RequestMapping("/discovery-client")
@RequiredArgsConstructor
public class MetricsAndInfoController {

    private static final Logger logger = LoggerFactory.getLogger(MetricsAndInfoController.class);

    private final RestTemplate restTemplate;
    private final ServiceInfoCollector serviceInfoCollector;
    private final ServiceMapper serviceMapper;
    private final MetricsCollector metricsCollector;

    @Value("${agent.service.url}")
    private String agentServiceUrl;

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        logger.info("Service registration triggered.");

        ServiceInfoDTO serviceInfoDTO = serviceInfoCollector.collectServiceInfo();
        ServiceInfoDTOResponce serviceInfoDTOResponce = serviceMapper.toServiceInfoDTO(serviceInfoDTO);

        String url = agentServiceUrl + "/ctx/api/v1/agent/register";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, serviceInfoDTOResponce, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                logger.info("Service registered successfully at agent.");
            } else {
                logger.error("Failed to register service at agent. Response: {}", response);
            }
        } catch (ResourceAccessException e) {
            logger.warn("Agent is not available. Skipping registration.");
        }
    }


    @GetMapping("/metrics")
    public ResponseEntity<MetricsDTOResponce> provideMetrics() {
        logger.info("Received request to provide metrics.");

        MetricsData metricsData = metricsCollector.collectMetrics();
        ServiceInfoDTO serviceInfoDTO = serviceInfoCollector.collectServiceInfo();
        MetricsDTOResponce metricsDTOResponce = serviceMapper.toMetricsDTO(metricsData, serviceInfoDTO);

        logger.info("Returning metrics: {}", metricsDTOResponce);

        return ResponseEntity.ok(metricsDTOResponce);
    }
}