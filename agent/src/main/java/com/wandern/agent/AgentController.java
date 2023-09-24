package com.wandern.agent;

import com.wandern.agent.health.ServiceHealthInfo;
import com.wandern.agent.health.ServiceHealthRegistry;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

/**
 * Controller responsible for handling service registration and metrics collection.
 * Provides endpoints to register services and periodically collects metrics from registered services.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final AgentService agentService;
    private final ServiceHealthRegistry serviceHealthRegistry;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        agentService.registerServiceInAgent(serviceInfoDTO);
        agentService.registerServiceInMaster(serviceInfoDTO);
        return ResponseEntity.ok("Service registered successfully in agent and master.");
    }

    @GetMapping("/services")
    public ResponseEntity<Collection<ServiceInfoDTO>> getAllRegisteredServices() {
        Collection<ServiceInfoDTO> allServices = agentService.getAllServices();
        return ResponseEntity.ok(allServices);
    }

    @PostMapping("/update-service-health")
    public ResponseEntity<Void> updateServiceHealth(@RequestBody ServiceHealthInfo serviceHealthInfo) {
        serviceHealthRegistry.updateServiceHealth(serviceHealthInfo.serviceName(), serviceHealthInfo.healthStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/services-health")
    public ResponseEntity<Map<String, ServiceHealthInfo>> getServicesHealth() {
        return ResponseEntity.ok(serviceHealthRegistry.getAllServiceHealthInfo());
    }
}
