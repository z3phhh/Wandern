package com.wandern.master;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Data
public class Metrics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemory;
    private long freeMemory;
    private int totalThreads;

    // Можете добавить дополнительные поля, такие как timestamp или идентификатор сервиса, от которого пришли метрики
    private LocalDateTime collectedAt = LocalDateTime.now();
    private String serviceDeploymentId;
}
