package com.wandern.clients;

import lombok.Builder;

@Builder
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