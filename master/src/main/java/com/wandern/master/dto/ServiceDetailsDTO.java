package com.wandern.master.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceDetailsDTO {
    private String deploymentId;
    private String deploymentUnit;
    private String system;
    private String serviceUrl;
    private String contextPath;
    private int port;
    private String ip;
    private String status;
    private Metrics2DTO metrics;
}