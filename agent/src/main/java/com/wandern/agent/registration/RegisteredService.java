package com.wandern.agent.registration;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "registered_service")
@Data
public class RegisteredService {

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
    private String serviceUrl;

    private int port;
//    private String ip;
    private LocalDateTime lastUpdate;
}
