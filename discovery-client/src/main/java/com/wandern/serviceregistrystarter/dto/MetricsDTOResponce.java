package com.wandern.serviceregistrystarter.dto;

public record MetricsDTOResponce(
    double systemLoad,
    double jvmCpuLoad,
    long usedMemoryBytes,
    long freeMemoryBytes,
    int totalThreads,
    String deploymentId,
    String system,
    String serviceUrl
) {}
