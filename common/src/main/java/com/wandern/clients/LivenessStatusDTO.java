package com.wandern.clients;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record LivenessStatusDTO (
    String status,
    LocalDateTime timestamp,
    String error,
    long latencyMillis
) {}