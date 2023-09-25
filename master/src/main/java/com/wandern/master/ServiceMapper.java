package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatus;
import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class ServiceMapper {
    public abstract RegisteredService toEntity(ServiceInfoDTO DTO);

    public abstract Metrics toEntity(MetricsDTO DTO);

    public abstract void updateMetricsFromDTO(MetricsDTO dto, @MappingTarget Metrics entity);

    @AfterMapping
    protected void setAdditionalFields(@MappingTarget RegisteredService entity) {
        entity.setRegistrationTime(LocalDateTime.now());
        entity.setStatus(ServiceStatus.UP);
    }
}
