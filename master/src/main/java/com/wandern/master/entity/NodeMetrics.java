package com.wandern.master.entity;

import com.wandern.clients.NodeMetricsDTO;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "node_metrics")
public class NodeMetrics {

    @Id
    @SequenceGenerator(
            name = "node_metrics_id_seq",
            sequenceName = "node_metrics_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "node_metrics_id_seq"
    )
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "node_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "fk_node_node_metrics"
            )
    )
    private Node node;

    private LocalDateTime lastUpdate;

    private double systemLoad;
    private double jvmCpuLoad;

    @Column(name = "used_memory_mb")
    private long usedMemoryMB;

    @Column(name = "free_memory_mb")
    private long freeMemoryMB;
    private int totalThreads;

}