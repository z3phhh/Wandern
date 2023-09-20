package com.wandern.agent;

import com.wandern.clients.ServiceInfoDTO;
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
public class ServiceRegistry {

    private final ConcurrentMap<String, ServiceInfoDTO> registry = new ConcurrentHashMap<>();

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        registry.put(serviceInfoDTO.deploymentId(), serviceInfoDTO);
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