package com.wandern.master.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "metrics_service")
public class Metrics {

    @Id
    @SequenceGenerator(
            name = "metrics_service_id_seq",
            sequenceName = "metrics_service_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "metrics_service_id_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "id",
            referencedColumnName = "registered_id",
            foreignKey = @ForeignKey(
                    name = "fk_metrics_service_registered_service"
            )
    )
    private RegisteredService registeredService;

    private LocalDateTime timestamp;

    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemoryBytes;
    private long freeMemoryBytes;
    private int totalThreads;
}
