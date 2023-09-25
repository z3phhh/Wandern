package com.wandern.agent.health;

import com.wandern.starter.health.HealthStatus;

/**
 * Класс для хранения информации о состоянии здоровья сервиса.
 */
public record ServiceHealthInfo (
    String serviceName,
    HealthStatus healthStatus
) {}