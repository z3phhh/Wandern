package com.wandern.clients;

public record MetricsDTO(
    double systemLoad,
    double jvmCpuLoad,
    long usedMemoryBytes,
    long freeMemoryBytes,
    int totalThreads
) {}
