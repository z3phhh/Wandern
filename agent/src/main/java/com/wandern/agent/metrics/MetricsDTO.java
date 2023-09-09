package com.wandern.agent.metrics;

public record MetricsDTO(
    String deploymentId,
    String system,
    String serviceUrl,
    double systemLoad,
    double jvmCpuLoad,
    long usedMemory,
    long freeMemory,
    int totalThreads
) {}
