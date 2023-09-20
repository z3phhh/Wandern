package com.wandern.master.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "metrics_service")
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
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @ManyToOne
    @JoinColumn(
            name = "registered_service_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "service_metrics_fk"
            )
    )
    private RegisteredService registeredService;

    private LocalDateTime timestamp;

    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemory;
    private long freeMemory;
    private int totalThreads;

}
