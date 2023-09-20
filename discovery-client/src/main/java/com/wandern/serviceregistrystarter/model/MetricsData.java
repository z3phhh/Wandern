package com.wandern.serviceregistrystarter.model;

/**
 * MetricsData is a record that encapsulates various system and JVM metrics.
 * 
 * @param systemLoad       The average system load.
 * @param jvmCpuLoad       The CPU load of the JVM.
 * @param usedMemoryBytes  The amount of used memory in bytes.
 * @param freeMemoryBytes  The amount of free memory in bytes.
 * @param totalThreads     The total number of threads.
 */
public record MetricsData(
        double systemLoad,
        double jvmCpuLoad,
        long usedMemoryBytes,
        long freeMemoryBytes,
        int totalThreads
)
{}
