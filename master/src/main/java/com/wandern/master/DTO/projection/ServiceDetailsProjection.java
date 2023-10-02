package com.wandern.master.DTO.projection;

import com.wandern.clients.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceDetailsProjection {
    private String deploymentId;
    private String deploymentUnit;
    private String system;
    private String serviceUrl;
    private String contextPath;
    private int port;
    private String ip;
    private ServiceStatus status;
    private MetricsProjection metrics;
}