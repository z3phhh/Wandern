package com.wandern.serviceregistrystarter.metrics;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MetricsCollector {

    private final OperatingSystemMXBean operatingSystemMXBean;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;

    public MetricsCollector() {
        this.operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public Map<String, Object> collectMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("systemLoad", operatingSystemMXBean.getSystemLoadAverage());
        metrics.put("jvmCpuLoad", getJvmCpuLoad());
        metrics.put("usedMemory", memoryMXBean.getHeapMemoryUsage().getUsed());
        metrics.put("freeMemory", getFreeMemory());
        metrics.put("totalThreads", threadMXBean.getThreadCount());
        return metrics;
    }

    private double getJvmCpuLoad() {
        if (operatingSystemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
            return ((com.sun.management.OperatingSystemMXBean) operatingSystemMXBean).getProcessCpuLoad();
        }
        return -1;
    }

    private long getFreeMemory() {
        return memoryMXBean.getHeapMemoryUsage().getMax() - memoryMXBean.getHeapMemoryUsage().getUsed();
    }
}
