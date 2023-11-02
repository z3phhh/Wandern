package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.NodeMetricsDTO;
import com.wandern.master.entity.Node;
import com.wandern.master.entity.NodeMetrics;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.repository.NodeMetricsRepository;
import com.wandern.master.repository.NodeRepository;
import com.wandern.master.repository.ResourceMetricsRepository;
import com.wandern.master.repository.RegisteredServiceRepository;
import com.wandern.model.ServiceInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class MasterService {

    private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final ResourceMetricsRepository resourceMetricsRepository;

    private final MasterMapper masterMapper;
    private final NodeRepository nodeRepository;
    private final NodeMetricsRepository nodeMetricsRepository;

    @Transactional
    @KafkaListener(
            topics = "${kafka.topic.node-metrics}",
            groupId = "${kafka.group.id}",
            containerFactory = "nodeMetricsDTOKafkaListenerContainerFactory")
    public void listenNodeMetrics(NodeMetricsDTO nodeMetricsDTO) {
        var node = nodeRepository.findByNodeIp(nodeMetricsDTO.nodeIp())
                .orElseGet(() -> masterMapper.toNode(nodeMetricsDTO));

        updateNodeWithData(node, nodeMetricsDTO);
        nodeRepository.save(node);

        var nodeMetrics = nodeMetricsRepository.findByNode(node)
                .orElseGet(() -> masterMapper.toNodeMetrics(nodeMetricsDTO.nodeMetrics(), node));

        updateNodeMetricsWithData(nodeMetrics, nodeMetricsDTO.nodeMetrics());
        nodeMetricsRepository.save(nodeMetrics);
    }

/*    public void registerService(ServiceInfo serviceInfo) {
        var node = nodeRepository.findByNodeIp(serviceInfo.ip())
                .orElseThrow(() -> new RuntimeException("Node not found with IP: " + serviceInfo.ip()));

        RegisteredService serviceInfoEntity = masterMapper.toServiceInfoEntity(serviceInfo, node);
        registeredServiceRepository.save(serviceInfoEntity);
    }*/

    public void registerService(ServiceInfo serviceInfo) {
        nodeRepository.findByNodeIp(serviceInfo.ip())
                .map(node -> {
                    RegisteredService serviceInfoEntity = masterMapper.toServiceInfoEntity(serviceInfo, node);
                    return registeredServiceRepository.save(serviceInfoEntity);
                })
                .orElseThrow(() -> new RuntimeException("Node not found with IP: " + serviceInfo.ip()));
    }


    /**
     * Сохраняет метрики для указанного сервиса.
     * Поиск существующих метрик осуществляется по идентификатору развертывания.
     * Если метрики уже существуют, они обновляются. В противном случае создается новая запись.
     *
     * @param deploymentId идентификатор развертывания сервиса.
     * @param metricsDTO метрики сервиса.
     * @return ответ о результате сохранения метрик.
     */
    @Transactional
    public ResponseEntity<String> saveMetrics(String deploymentId, MetricsDTO metricsDTO) {
        var registeredService = getRegisteredServiceOrThrow(deploymentId);

        var metrics = resourceMetricsRepository.findByRegisteredService(registeredService)
                .map(existingResourceMetrics -> {
                    MasterMapper.updateMetricsFromDTO(metricsDTO, existingResourceMetrics);
                    return existingResourceMetrics;
                })
                .orElseGet(() -> {
                    var newMetrics = MasterMapper.toEntity(metricsDTO);
                    newMetrics.setRegisteredService(registeredService);
                    return newMetrics;
                });
        metrics.setTimestamp(LocalDateTime.now());

        try {
            resourceMetricsRepository.save(metrics);
            logger.info("Metrics saved successfully for deploymentId: {}", deploymentId);
            return ResponseEntity.ok("Metrics saved successfully.");
        } catch (Exception e) {
            logger.error("Error saving metrics for deploymentId: {}", deploymentId, e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving metrics for deploymentId: " + deploymentId);
        }
    }

    private RegisteredService getRegisteredServiceOrThrow(String deploymentId) {
        return registeredServiceRepository.findByDeploymentId(deploymentId)
                .orElseThrow(() -> {
                    logger.warn("No RegisteredService found for deploymentId: {}", deploymentId);
                    return new RuntimeException("RegisteredService not found for deploymentId: " + deploymentId);
                });
    }

    private void updateNodeWithData(Node node, NodeMetricsDTO dto) {
        node.setNodeIp(dto.nodeIp());
        node.setNodeId(dto.nodeId());
        node.setTotalServices((int) dto.totalServices());
        node.setActiveServices((int) dto.activeServices());
        node.setInactiveServices((int) dto.inactiveServices());
        node.setLastUpdate(LocalDateTime.now());
    }

    private void updateNodeMetricsWithData(NodeMetrics nodeMetrics, MetricsDTO metrics) {
        nodeMetrics.setSystemLoad(metrics.systemLoad());
        nodeMetrics.setJvmCpuLoad(metrics.jvmCpuLoad());
        nodeMetrics.setUsedMemoryMB(metrics.usedMemoryMB());
        nodeMetrics.setFreeMemoryMB(metrics.freeMemoryMB());
        nodeMetrics.setTotalThreads(metrics.totalThreads());
        nodeMetrics.setLastUpdate(LocalDateTime.now());
    }

/*    @Transactional
    public void updateServiceStatus(ServiceStatusDTO statusUpdate) {
        Optional.ofNullable(statusUpdate)
                .filter(update -> "DOWN".equals(update.status()))
                .ifPresent(update -> {
                    // Найти сервис по его deploymentId
                    registeredServiceRepository.findByDeploymentId(update.deploymentId())
                            .ifPresent(service -> {
                                service.setStatus(ServiceStatus.DOWN);
                                registeredServiceRepository.save(service);
                                logger.info("Service with deploymentId: {} has been set to DOWN status.", update.deploymentId());
                            });
                });
    }*/
}