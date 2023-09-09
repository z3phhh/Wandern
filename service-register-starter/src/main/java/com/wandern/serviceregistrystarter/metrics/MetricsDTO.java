package com.wandern.serviceregistrystarter.metrics;

public record MetricsDTO(
    double systemLoad,
    double jvmCpuLoad,
    long usedMemory,
    long freeMemory,
    int totalThreads,
    String deploymentId,
    String system,
    String serviceUrl
) {}
