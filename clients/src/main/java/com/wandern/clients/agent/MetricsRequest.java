package com.wandern.clients.agent;

public record MetricsRequest(
    String deploymentId,
    String system,
    String serviceUrl,
    double systemLoad,
    double jvmCpuLoad,
    long usedMemory,
    long freeMemory,
    int totalThreads
) {}