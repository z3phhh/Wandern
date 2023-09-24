package com.wandern.starter.health;

import org.springframework.boot.actuate.health.Status;

// мб в комон вынести, пока пусть тут живет
public record HealthStatus(
    Status status,
    String details
) {}
