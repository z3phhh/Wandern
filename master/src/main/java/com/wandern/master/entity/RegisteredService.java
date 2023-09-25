package com.wandern.master.entity;

import com.wandern.clients.ServiceStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "registered_service",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "deployment_id_unit_unique",
                        columnNames = "deployment_id")
        }
)
public class RegisteredService {

    @Id
    @SequenceGenerator(
            name = "registered_service_id_sequence",
            sequenceName = "registered_service_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "registered_service_id_sequence"
    )
    private Long id;

    @Column(
            name = "deployment_id",
            nullable = false
    )
    private String deploymentId;

    @OneToOne(
            mappedBy = "registeredService",
            orphanRemoval = true
    )
    private Metrics metrics;

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    private String system;
    private String deploymentUnit;
    private String serviceUrl;
    private String contextPath;

    private int port;
    private String ip;
    private LocalDateTime registrationTime;
}
