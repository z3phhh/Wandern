package com.wandern.clients;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ServiceInfoDTO(
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
) {
}