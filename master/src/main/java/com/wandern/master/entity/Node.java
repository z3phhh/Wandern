package com.wandern.master.entity;

import com.wandern.clients.NodeMetricsDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "node")
public class Node {

    @Id
    @SequenceGenerator(
            name = "node_id_seq",
            sequenceName = "node_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "node_id_seq"
    )
    private Long id;


    private String nodeIp;
    private String nodeId;
    private Integer totalServices;
    private Integer activeServices;
    private Integer inactiveServices;
    private LocalDateTime lastUpdate;

    /*    @Id
    private String id;*/

/*    @OneToMany(
            mappedBy = "node",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<RegisteredService> registeredService = new ArrayList<>();*/

/*    @OneToOne(
            mappedBy = "node",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE}
    )
    private NodeMetrics nodeMetrics;*/
}