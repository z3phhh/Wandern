package com.wandern.agent.health;

import com.wandern.starter.health.HealthStatus;

public record ServiceHealthInfo (
    String serviceName,
    HealthStatus healthStatus
) {}