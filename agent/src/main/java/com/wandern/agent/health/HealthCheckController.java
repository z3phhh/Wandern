package com.wandern.agent.health;

import com.wandern.clients.LivenessStatusDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/health")
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    @GetMapping("/{deploymentId}")
    public LivenessStatusDTO getServiceStatus(@PathVariable String deploymentId) {
        return healthCheckService.getLivenessStatusDTO(deploymentId);
    }

    @GetMapping
    public Map<String, LivenessStatusDTO> getAllServicesStatus() {
        return healthCheckService.getAllLivenessStatusesDTO();
    }
}
