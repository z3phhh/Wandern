package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.health.HealthStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MasterController {

    private final MasterService masterService;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        return masterService.registerService(serviceInfoDTO);
    }

    @PostMapping("/{deploymentId}/metrics")
    public ResponseEntity<String> saveMetrics(@PathVariable String deploymentId,
                                              @RequestBody MetricsDTO metricsDTO) {
        return masterService.saveMetrics(deploymentId, metricsDTO);
    }

    @PostMapping("/health/status/{deploymentId}")
    public ResponseEntity<String> updateServiceStatus(@PathVariable String deploymentId,
                                                      @RequestBody HealthStatus healthStatus) {
        return masterService.updateServiceStatus(deploymentId, healthStatus);
    }

/*    @PostMapping("/status")
    public ResponseEntity<String> updateMultipleServiceStatus(@RequestBody Map<String, HealthStatus> healthStatusMap) {
        try {
            for (Map.Entry<String, HealthStatus> entry : healthStatusMap.entrySet()) {
                masterService.updateServiceStatus(entry.getKey(), entry.getValue());
            }
            return ResponseEntity.ok("Service statuses updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to update service statuses.");
        }
    }*/

}