package com.wandern.agent.health;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LivenessStatus {
    private String status;
    private LocalDateTime timestamp;
    private String error;
    private long latencyMillis;
}