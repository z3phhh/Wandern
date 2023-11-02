package com.wandern.model;

import com.wandern.clients.ServiceStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ServiceInfo(
    String deploymentId,
    String deploymentUnit,
    String system,
    String serviceUrl,
    String contextPath,
    int port,
    String ip,
    ServiceStatus status,
    String programmingLanguage,
    LocalDateTime registrationTime
) {}