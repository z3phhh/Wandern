package com.wandern.clients;

import lombok.Builder;

@Builder
public record MetricsDTO (
    double systemLoad,
    double jvmCpuLoad,
    long usedMemoryMB,
    long freeMemoryMB,
    int totalThreads
){}
