package com.wandern.serviceregistrystarter.metrics;

import com.wandern.serviceregistrystarter.DTOMapper;
import com.wandern.serviceregistrystarter.ServiceInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private static final Logger logger = LoggerFactory.getLogger(MetricsController.class);

    private final MetricsCollector metricsCollector;
    private final DTOMapper dtoMapper;
    private final ServiceInfo serviceInfo;


    @GetMapping
    public ResponseEntity<MetricsDTO> provideMetrics() {
        logger.info("Received request to provide metrics.");

        Map<String, Object> rawMetrics = metricsCollector.collectMetrics();
        MetricsDTO metricsDto = dtoMapper.mapToMetricsDTO(rawMetrics, serviceInfo);

        logger.info("Returning metrics: {}", metricsDto);

        return ResponseEntity.ok(metricsDto);
    }
}


