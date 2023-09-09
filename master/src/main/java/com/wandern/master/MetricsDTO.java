package com.wandern.master;

public record MetricsDTO(
    double systemLoad,
    double jvmCpuLoad,
    long usedMemory,
    long freeMemory,
    int totalThreads
) {}
