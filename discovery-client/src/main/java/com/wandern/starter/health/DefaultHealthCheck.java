package com.wandern.starter.health;

import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
public class DefaultHealthCheck implements HealthCheck {

    @Override
    public HealthStatus check() {
        return new HealthStatus(Status.UP, "Service is running");
    }
}