package com.wandern.serviceregistrystarter.dto;

public record ServiceInfoDTOResponce(
    String deploymentId,
    String deploymentUnit,
    String system,
    String serviceUrl,
    String contextPath,
    int port,
    String ip
)
{}

