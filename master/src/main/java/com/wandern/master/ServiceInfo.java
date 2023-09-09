package com.wandern.master;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "registered_service")
public class ServiceInfo {
    @Id
    @SequenceGenerator(
            name = "service_info_id_sequence",
            sequenceName = "service_info_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "service_info_id_sequence"
    )
    private Long id;

    private String system;
    private String deploymentId;
//    private String name;
    private String serviceUrl;
//    private String ip;
    private int port;
//    private LocalDateTime lastHeartbeat;
}
