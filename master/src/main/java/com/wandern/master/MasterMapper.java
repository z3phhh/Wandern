package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.NodeMetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.master.entity.Node;
import com.wandern.master.entity.NodeMetrics;
import com.wandern.master.entity.ResourceMetrics;
import com.wandern.master.entity.RegisteredService;
import com.wandern.model.ServiceInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class MasterMapper {

    public static RegisteredService toEntity(ServiceInfoDTO DTO) {
        return RegisteredService.builder()
                .deploymentId(DTO.deploymentId())
                .deploymentUnit(DTO.deploymentUnit())
                .system(DTO.system())
                .serviceUrl(DTO.serviceUrl())
                .contextPath(DTO.contextPath())
                .port(DTO.port())
                .ip(DTO.ip())
//                .status(ServiceStatus.UP)
                .registrationTime(LocalDateTime.now())
                .build();
    }

    public static ResourceMetrics toEntity(MetricsDTO DTO) {
        return ResourceMetrics.builder()
                .systemLoad(DTO.systemLoad())
                .jvmCpuLoad(DTO.jvmCpuLoad())
                .usedMemoryMB(DTO.usedMemoryMB())
                .freeMemoryMB(DTO.freeMemoryMB())
                .totalThreads(DTO.totalThreads())
                .build();
    }

    public static void updateMetricsFromDTO(MetricsDTO DTO, ResourceMetrics resourceMetrics) {
        resourceMetrics.setSystemLoad(DTO.systemLoad());
        resourceMetrics.setJvmCpuLoad(DTO.jvmCpuLoad());
        resourceMetrics.setUsedMemoryMB(DTO.usedMemoryMB());
        resourceMetrics.setFreeMemoryMB(DTO.freeMemoryMB());
        resourceMetrics.setTotalThreads(DTO.totalThreads());
    }

    public Node toNode(NodeMetricsDTO nodeMetricsDTO) {
        return Node.builder()
                .nodeId(nodeMetricsDTO.nodeId())
                .nodeIp(nodeMetricsDTO.nodeIp())
                .totalServices((int) nodeMetricsDTO.totalServices())
                .activeServices((int) nodeMetricsDTO.activeServices())
                .inactiveServices((int) nodeMetricsDTO.inactiveServices())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    public NodeMetrics toNodeMetrics(MetricsDTO metrics, Node node) {
        return NodeMetrics.builder()
                .node(node)
                .systemLoad(metrics.systemLoad())
                .jvmCpuLoad(metrics.jvmCpuLoad())
                .usedMemoryMB(metrics.usedMemoryMB())
                .freeMemoryMB(metrics.freeMemoryMB())
                .totalThreads(metrics.totalThreads())
                .lastUpdate(LocalDateTime.now())
                .build();
    }

    public RegisteredService toServiceInfoEntity(ServiceInfo serviceInfo, Node node) {
        return RegisteredService.builder()
                .deploymentId(serviceInfo.deploymentId())
                .deploymentUnit(serviceInfo.deploymentUnit())
                .system(serviceInfo.system())
                .serviceUrl(serviceInfo.serviceUrl())
                .contextPath(serviceInfo.contextPath())
                .port(serviceInfo.port())
                .ip(serviceInfo.ip())
                .programmingLanguage(serviceInfo.programmingLanguage())
                .registrationTime(serviceInfo.registrationTime())
                .node(node)
                .build();
    }
}