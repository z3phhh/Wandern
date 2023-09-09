package com.wandern.master;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MasterController {

    private final MasterService masterService;

    @PostMapping("/services/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDto) {
        masterService.registerService(serviceInfoDto);
        return ResponseEntity.ok("Service registered successfully");
    }

    @PostMapping("/metrics/collect")
    public ResponseEntity<String> collectMetrics(@RequestBody MetricsDTO metricsDto) {
        masterService.collectMetrics(metricsDto);
        return ResponseEntity.ok("Metrics collected successfully");
    }
}
