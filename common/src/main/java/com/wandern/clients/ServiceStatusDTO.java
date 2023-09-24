package com.wandern.clients;

public record ServiceStatusDTO (
    String deploymentId,
    String status
) {}