package com.wandern.agent.health;

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
    private final Map<String, ServiceHealthInfo> serviceHealthInfoMap = new ConcurrentHashMap<>();

    /**
     * Метод для обновления состояний здоровья сервисов
     * @param serviceName
     * @param healthStatus
     */
    public void updateServiceHealth(String serviceName, HealthStatus healthStatus) {
        serviceHealthInfoMap.put(serviceName, new ServiceHealthInfo(serviceName, healthStatus));
    }

    /**
     * Мето для обновления состояний здоровья сервисов
     * @return
     */
    public Map<String, ServiceHealthInfo> getAllServiceHealthInfo() {
        return Collections.unmodifiableMap(serviceHealthInfoMap);
    }
}
