package com.wandern.starter;

import java.lang.management.*;

import com.wandern.clients.MetricsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MetricsCollector {

    private final OperatingSystemMXBean operatingSystemMXBean;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;

    public MetricsCollector() {
        this.operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public MetricsDTO collectMetrics() {
        double systemLoad = operatingSystemMXBean.getSystemLoadAverage();
        double jvmCpuLoad = getJvmCpuLoad();
        long usedMemoryBytes = memoryMXBean.getHeapMemoryUsage().getUsed();
        long freeMemoryBytes = getFreeMemory();
        int totalThreads = threadMXBean.getThreadCount();

        MetricsDTO metricsDTO = new MetricsDTO(
                systemLoad,
                jvmCpuLoad,
                usedMemoryBytes,
                freeMemoryBytes,
                totalThreads
        );

//        logger.info("Collected Metrics: {}", metricsDTO);

        return metricsDTO;
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


