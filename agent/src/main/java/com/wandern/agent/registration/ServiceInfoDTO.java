package com.wandern.agent.registration;

public record ServiceInfoDTO(
    String deploymentId,
    String system,
    String serviceUrl,
    int port
) {}
