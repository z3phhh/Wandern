package com.wandern.master.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "registered_service")
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
            name = "deploymentId",
            updatable = false,
            nullable = false
    )
    private String deploymentId;

    @OneToMany(
            mappedBy = "registeredService",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<MetricsEntity> metrics = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ServiceStatus status;

    private String system;

//    @Column(
//            name = "deploymentUnit",
//            updatable = false,
//            nullable = false,
//            unique = true
//    )
    private String deploymentUnit;
    private String serviceUrl;
    private String contextPath;

    private int port;
    private String ip;
    private LocalDateTime registrationTime;
//    private LocalDateTime deregistrationTime;
}
