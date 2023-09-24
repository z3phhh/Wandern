package com.wandern.agent;

import com.wandern.agent.health.HealthCheckAgent;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * A registry for managing and storing service information.
 * Provides methods to register, retrieve, and remove services.
 */
@Component
@RequiredArgsConstructor
public class ServiceRegistry {

    private final ConcurrentMap<String, ServiceInfoDTO> registry = new ConcurrentHashMap<>();
    private final HealthCheckAgent healthCheckAgent;

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        registry.put(serviceInfoDTO.deploymentId(), serviceInfoDTO);
        healthCheckAgent.registerService(serviceInfoDTO); // для отслеживания
    }


    public Optional<ServiceInfoDTO> getService(String deploymentId) {
        return Optional.ofNullable(registry.get(deploymentId));
    }

    public Collection<ServiceInfoDTO> getAllServices() {
        return registry.values();
    }

    public void removeService(String deploymentId) {
        registry.remove(deploymentId);
    }
}