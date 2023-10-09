package com.wandern.agent.registry;

import com.wandern.agent.health.model.HealthInfo;
import com.wandern.starter.health.HealthStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реестр, хранящий информацию о состоянии здоровья всех зарегистрированных сервисов.
 */
@Component
public class ServiceHealthRegistry {

    /**
     * Map serviceHealthInfoMap хранит информацию о состоянии здоровья сервисов
     */
    private final Map<String, HealthInfo> serviceHealthInfoMap = new ConcurrentHashMap<>();

    /**
     * Метод для обновления состояний здоровья сервисов
     * @param serviceName
     * @param healthStatus
     */
    public void updateServiceHealth(String serviceName, HealthStatus healthStatus) {
        serviceHealthInfoMap.put(serviceName, new HealthInfo(serviceName, healthStatus));
    }

    /**
     * Мето для обновления состояний здоровья сервисов
     * @return
     */
    public Map<String, HealthInfo> getAllServiceHealthInfo() {
        return Collections.unmodifiableMap(serviceHealthInfoMap);
    }
}
