package com.wandern.agent.health.model;

import com.wandern.starter.health.HealthStatus;

/**
 * Класс для хранения информации о состоянии здоровья сервиса.
 */
public record HealthInfo(
    String serviceName,
    HealthStatus healthStatus
) {}