package com.wandern.agent.registration;

import com.wandern.agent.metrics.MetricsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
@RequiredArgsConstructor
public class ServiceRegistrationController {

    private final ServiceRegistrationService serviceRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDto) {
        serviceRegistrationService.registerServiceInAgentDB(serviceInfoDto);
        serviceRegistrationService.registerServiceWithMaster(serviceInfoDto);
        return ResponseEntity.ok("Service registered successfully");
    }

    @Deprecated // a little later :(
    @PostMapping("/collect-metrics")
    public ResponseEntity<String> collectMetrics(@RequestBody MetricsDTO metricsDto) {
        // logic ...
        serviceRegistrationService.saveMetrics(metricsDto, metricsDto.deploymentId(), metricsDto.system(), metricsDto.serviceUrl());
        return ResponseEntity.ok("Metrics collected and saved");
    }
}
