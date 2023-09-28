package com.wandern.clients;

public record ServiceInfoDTO(
    String deploymentId,
    String deploymentUnit,
    String system,
    String serviceUrl,
    String contextPath,
    int port,
    String ip,
    ServiceStatus status
) {
}