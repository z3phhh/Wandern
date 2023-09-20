package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.master.entity.MetricsEntity;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.repository.MetricsRepository;
import com.wandern.master.repository.RegisteredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterService {

    private static final Logger logger = LoggerFactory.getLogger(MasterService.class);

    private final RegisteredServiceRepository registeredServiceRepository;
    private final MetricsRepository metricsRepository;
    private final ServiceMapper serviceMapper;

    public void registerService(ServiceInfoDTO serviceInfoDTO) {
        logger.info("Original ServiceInfoData: {}", serviceInfoDTO);
        RegisteredService registeredService = serviceMapper.toEntity(serviceInfoDTO);
        logger.info("Mapped to RegisteredService entity: {}", registeredService);

        registeredServiceRepository.save(registeredService);

        logger.info("Service registered successfully with ID: {}", registeredService.getDeploymentId());
    }

    public void saveMetrics(MetricsDTO metricsDTO) {
        logger.info("Received metrics: {}", metricsDTO);
        MetricsEntity metricsEntity = serviceMapper.toEntity(metricsDTO);
        logger.info("Mapped to MetricsEntity: {}", metricsEntity);

        metricsRepository.save(metricsEntity);
        logger.info("Metrics saved successfully with ID: {}", metricsEntity.getId());
    }
}