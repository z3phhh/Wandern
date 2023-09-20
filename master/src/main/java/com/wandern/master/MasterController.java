package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MasterController {

    private final MasterService masterService;

    private static final Logger logger = LoggerFactory.getLogger(MasterController.class);

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        logger.info("Received registration request for service: {}", serviceInfoDTO);

        try {
            masterService.registerService(serviceInfoDTO);
            return ResponseEntity.ok("Service registered successfully in master.");
        } catch (Exception e) {
            logger.error("Error registering service: {}", serviceInfoDTO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register service in master.");
        }
    }

    @PostMapping("/metrics")
    public ResponseEntity<String> saveMetrics(@RequestBody MetricsDTO metricsDTO) {
        logger.info("Received metrics: {}", metricsDTO);

        try {
            masterService.saveMetrics(metricsDTO);
            return ResponseEntity.ok("Metrics saved successfully in master.");
        } catch (Exception e) {
            logger.error("Error saving metrics: {}", metricsDTO, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save metrics in master.");
        }
    }

}