package com.wandern.starter;

import java.lang.management.*;

import com.wandern.clients.MetricsDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class MetricsCollector {

    private static final Logger logger = LoggerFactory.getLogger(MetricsCollector.class);

    private final OperatingSystemMXBean operatingSystemMXBean;
    private final MemoryMXBean memoryMXBean;
    private final ThreadMXBean threadMXBean;

    public MetricsCollector() {
        this.operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }

    public MetricsDTO collectMetrics() {
        double loadAverage = operatingSystemMXBean.getSystemLoadAverage();
        logger.info("loadAverage: {}", loadAverage);
        int availableProcessors = operatingSystemMXBean.getAvailableProcessors();
        logger.info("availableProcessors: {}", availableProcessors);

        double rawSystemLoad = (loadAverage / availableProcessors) * 100;
        double systemLoad = Math.floor(rawSystemLoad * 100) / 100.0;

        double jvmCpuLoad = Math.round(getJvmCpuLoad() * 10000) / 100.0;
        long usedMemoryMB = memoryMXBean.getHeapMemoryUsage().getUsed() / (1024 * 1024);
        long freeMemoryMB = getFreeMemory() / (1024 * 1024);
        int totalThreads = threadMXBean.getThreadCount();

        return new MetricsDTO(
                systemLoad,
                jvmCpuLoad,
                usedMemoryMB,
                freeMemoryMB,
                totalThreads
        );
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


