package com.wandern.agent.metrics;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "metrics_service")
@Entity
@Data
public class MetricsEntity {

    @Id
    @SequenceGenerator(
            name = "system_metrics_id_sequence",
            sequenceName = "system_metrics_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "system_metrics_id_sequence"
    )
    private Long id;
    private String deploymentId; //unique
    private String system;
    private String serviceUrl;
    private LocalDateTime timestamp;

    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemory;
    private long freeMemory;
    private int totalThreads;
}
