package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatusDTO;
import com.wandern.master.repository.RegisteredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MasterController {

    private final MasterService masterService;

    private final RegisteredServiceRepository registeredServiceRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        return masterService.registerService(serviceInfoDTO);
    }

    @PostMapping("/{deploymentId}/metrics")
    public ResponseEntity<String> saveMetrics(@PathVariable String deploymentId,
                                              @RequestBody MetricsDTO metricsDTO) {
        return masterService.saveMetrics(deploymentId, metricsDTO);
    }

    @Transactional
    @PostMapping("/status/update")
    public ResponseEntity<Void> updateServiceStatus(@RequestBody ServiceStatusDTO statusUpdate) {
        if ("DOWN".equals(statusUpdate.status())) {
            registeredServiceRepository.deleteByDeploymentId(statusUpdate.deploymentId());
            // + логи
        }
        return ResponseEntity.ok().build();
    }

}