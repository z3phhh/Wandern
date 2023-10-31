package com.wandern.master.entity;

import com.wandern.clients.ServiceStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        name = "registered_service",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_deployment_id",
                        columnNames = "deployment_id"
                )
        }
)
public class RegisteredService {

    @Id
    @SequenceGenerator(
            name = "registered_service_id_seq",
            sequenceName = "registered_service_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "registered_service_id_seq"
    )
    private Long id;

    private String programmingLanguage;

    @Column(
            name = "deployment_id",
            nullable = false,
            columnDefinition = "VARCHAR(255)"
    )
    private String deploymentId;


    @ManyToOne
    @JoinColumn(
            name = "node_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_node_registered_service"
            )
    )
    private Node node;

    @OneToOne(
            mappedBy = "registeredService",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private ResourceMetrics resourceMetrics;

    @OneToOne(
            mappedBy = "registeredService",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}

    )
    private HealthMetrics healthMetrics;


    private String system;
    private String deploymentUnit;
    private String serviceUrl;
    private String contextPath;

    private int port;
    private String ip;
    private LocalDateTime registrationTime;
//    private LocalDateTime lastHealthCheck;

    // TODO: + lastHealthCheck, + language, + nodeId
}
