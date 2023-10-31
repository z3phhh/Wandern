package com.wandern.model;

import lombok.Builder;

@Builder
public record ServiceMetrics(
    String deploymentId,
    double systemLoad,
    double jvmCpuLoad,
    long usedMemoryMB,
    long freeMemoryMB,
    int totalThreads
){}