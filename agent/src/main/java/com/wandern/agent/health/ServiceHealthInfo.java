package com.wandern.agent.health;

import com.wandern.serviceregistrystarter.health.HealthStatus;

public record ServiceHealthInfo (
    String serviceName,
    HealthStatus healthStatus
) {}