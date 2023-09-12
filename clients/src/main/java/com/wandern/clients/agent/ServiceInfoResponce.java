package com.wandern.clients.agent;

public record ServiceInfoResponce(
    String deploymentId,
    String system,
    String serviceUrl,
    int port
) {}