package com.wandern.serviceregistrystarter;

public record ServiceInfoDTO(
        String deploymentId,
        String system,
        String serviceUrl,
        int port
) {}

