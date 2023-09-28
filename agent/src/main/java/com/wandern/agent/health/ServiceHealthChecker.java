package com.wandern.agent.health;

import com.wandern.clients.ServiceInfoDTO;
import org.springframework.boot.actuate.health.Status;

public interface ServiceHealthChecker {
    Status checkServiceHealth(ServiceInfoDTO serviceInfoDTO);
}
