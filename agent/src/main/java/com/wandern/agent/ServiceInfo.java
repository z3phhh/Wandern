package com.wandern.agent;

import lombok.Data;

@Data
public class ServiceInfo {
    private String system;
    private String deploymentId;
    private String address;
    private int port;
    private String healthEndpoint;
    private boolean isBalancingEnabled;
}