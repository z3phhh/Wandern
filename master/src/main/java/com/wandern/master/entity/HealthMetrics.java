package com.wandern.master.entity;

import com.wandern.clients.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "health_metrics")
public class HealthMetrics {

    @Id
    @SequenceGenerator(
            name = "health_metrics_id_seq",
            sequenceName = "health_metrics_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "health_metrics_id_seq"
    )
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "service_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_registered_service_health_metrics"
            )
    )
    private RegisteredService registeredService;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    private Integer latency;
    private LocalDateTime lastUpdate;

}