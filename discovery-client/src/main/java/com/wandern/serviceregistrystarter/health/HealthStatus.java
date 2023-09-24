package com.wandern.serviceregistrystarter.health;

import org.springframework.boot.actuate.health.Status;

// возможно не от сюда стоит статус брать
public record HealthStatus(
    Status status,
    String details
) {}
