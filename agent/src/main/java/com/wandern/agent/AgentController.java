package com.wandern.agent;

import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controller responsible for handling service registration and metrics collection.
 * Provides endpoints to register services and periodically collects metrics from registered services.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final AgentService agentService;

    /**
     * Registers a service in the agent and attempts to register it in the master.
     *
     * @param serviceInfoDTO The service information to be registered.
     * @return ResponseEntity with a success message if registration is successful, otherwise returns a partial success message.
     */
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
}