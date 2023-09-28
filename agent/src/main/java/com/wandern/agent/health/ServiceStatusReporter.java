package com.wandern.agent.health;

import org.springframework.boot.actuate.health.Status;

public interface ServiceStatusReporter {
    void sendStatusToMaster(String deploymentId, Status status);
}