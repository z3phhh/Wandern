package com.wandern.master.DTO.projection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MetricsProjection {
    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemoryMB;
    private long freeMemoryMB;
    private int totalThreads;
}