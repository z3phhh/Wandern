package com.wandern.agent;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AgentInfo {

    private String agentId;
    private LocalDateTime lastActive;
    private String agentVersion;
    private String status;
    private String nodeIp;
    private int agentPort;

    public AgentInfo(String nodeIp, int agentPort) {
        this.nodeIp = nodeIp;
        this.agentPort = agentPort;
        this.agentId = generateAgentId();
        this.lastActive = LocalDateTime.now();
        this.agentVersion = getAgentVersion();
        this.status = "active";
    }

    private String generateAgentId() {
        return nodeIp + ":" + agentPort;
    }

    private String getAgentVersion() {
        return "1.0.0";
    }
}