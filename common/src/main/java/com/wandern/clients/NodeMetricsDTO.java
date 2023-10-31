package com.wandern.clients;

import lombok.Builder;

@Builder
public record NodeMetricsDTO(
    String nodeId,
    String nodeIp,
    long totalServices,
    long activeServices,
    long inactiveServices,
    MetricsDTO nodeMetrics
) {}