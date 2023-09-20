package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.master.entity.MetricsEntity;
import com.wandern.master.entity.RegisteredService;
import com.wandern.master.entity.ServiceStatus;
import com.wandern.master.repository.RegisteredServiceRepository;
import org.mapstruct.*;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public abstract class ServiceMapper {
    public abstract RegisteredService toEntity(ServiceInfoDTO DTO);

    public abstract ServiceInfoDTO toDTO(RegisteredService entity);

    public abstract MetricsEntity toEntity(MetricsDTO DTO);


    @AfterMapping
    protected void setAdditionalFields(@MappingTarget RegisteredService entity) {
        entity.setRegistrationTime(LocalDateTime.now());
        entity.setStatus(ServiceStatus.UP);
    }
}
