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
 * Класс, отвечающий за регистрацию сервисов.
 * Хранит информацию о всех зарегистрированных сервисах и предоставляет функциональность для их регистрации.
 */
@Component
@RequiredArgsConstructor
public class ServiceRegistry {

    private final ConcurrentMap<String, ServiceInfoDTO> registry = new ConcurrentHashMap<>();
    private final HealthCheckAgent healthCheckAgent;

    /**
     * Регистрирует сервис в реестре и инициирует его отслеживание через HealthCheckAgent.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     */
    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        registry.put(serviceInfoDTO.deploymentId(), serviceInfoDTO);
        healthCheckAgent.registerService(serviceInfoDTO); // для hc
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