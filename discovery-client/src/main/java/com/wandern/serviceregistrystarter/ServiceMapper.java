package com.wandern.serviceregistrystarter;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.serviceregistrystarter.dto.MetricsDTOResponce;
import com.wandern.serviceregistrystarter.dto.ServiceInfoDTOResponce;
import com.wandern.serviceregistrystarter.model.MetricsData;
import org.mapstruct.Mapper;

//@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
@Mapper(componentModel = "spring")
public interface ServiceMapper {

    MetricsDTOResponce toMetricsDTO(MetricsData metricsData, ServiceInfoDTO serviceInfoDTO);

    ServiceInfoDTOResponce toServiceInfoDTO(ServiceInfoDTO serviceInfoDTO);

}
