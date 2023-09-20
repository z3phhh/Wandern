package com.wandern.serviceregistrystarter;

import java.lang.management.*;

import com.wandern.serviceregistrystarter.model.MetricsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


// TODO: вроде норм, мб логгер удалить все равно не используется
@Component
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

    public MetricsData collectMetrics() {
        double systemLoad = operatingSystemMXBean.getSystemLoadAverage();
        double jvmCpuLoad = getJvmCpuLoad();
        long usedMemoryBytes = memoryMXBean.getHeapMemoryUsage().getUsed();
        long freeMemoryBytes = getFreeMemory();
        int totalThreads = threadMXBean.getThreadCount();

        MetricsData metricsData = new MetricsData(
                systemLoad,
                jvmCpuLoad,
                usedMemoryBytes,
                freeMemoryBytes,
                totalThreads
        );

        logger.info("Collected Metrics: {}", metricsData);

        return metricsData;
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


