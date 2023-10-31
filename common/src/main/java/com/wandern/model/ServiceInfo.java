package com.wandern.model;

import com.wandern.clients.ServiceStatus;
import lombok.Builder;

@Builder
public record ServiceInfo(
    String deploymentId,
    String deploymentUnit,
    String system,
    String serviceUrl,
    String contextPath,
    int port,
    String ip,
    ServiceStatus status
) {}