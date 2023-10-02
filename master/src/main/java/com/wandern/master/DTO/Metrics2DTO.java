package com.wandern.master.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Metrics2DTO {
    private double systemLoad;
    private double jvmCpuLoad;
    private long usedMemoryMB;
    private long freeMemoryMB;
    private int totalThreads;
}