package com.wandern.master;

public record ServiceInfoDTO(
    String deploymentId,
    String system,
    String serviceUrl,
    int port
) {}
