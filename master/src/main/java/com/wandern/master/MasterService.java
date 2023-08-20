package com.wandern.master;

import com.wandern.agent.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class MasterService {

    private final Map<String, ServiceInfo> globalServices = new ConcurrentHashMap<>();

    public String registerService(ServiceInfo serviceInfo) {
        String serviceKey = serviceInfo.getSystem() + "-" + serviceInfo.getDeploymentId();
        globalServices.put(serviceKey, serviceInfo);
        log.info("Service registered: {}", serviceInfo);
        return "Service registered";
    }

    public void updateTopology(Map<String, ServiceInfo> topologyUpdates) {
        globalServices.putAll(topologyUpdates);
        log.info("Topology updated: {}", globalServices);
    }

    public Map<String, ServiceInfo> getTopology() {
        return globalServices;
    }

    public void updateHealthStatus(Map<String, Boolean> healthStatusUpdates) {
        for (Map.Entry<String, Boolean> entry : healthStatusUpdates.entrySet()) {
            ServiceInfo serviceInfo = globalServices.get(entry.getKey());
            if (serviceInfo != null) {
                serviceInfo.setBalancingEnabled(entry.getValue());
            }
        }
    }

    public ResponseEntity<String> updateBalancingStatus(Map<String, Object> requestBody) {
        log.info("Received request to update balancing status: {}", requestBody);
        String deploymentId = (String) requestBody.get("deploymentId");
        log.info("Extracted deploymentId: {}", deploymentId);
        boolean balancingEnabled = (boolean) requestBody.get("balancingEnabled");
        String serviceKey = null;
        for (String key : globalServices.keySet()) {
            if (key.endsWith("-" + deploymentId)) {
                serviceKey = key;
                break;
            }
        }
        log.info("Determined serviceKey: {}", serviceKey);
        if (serviceKey == null) {
            return new ResponseEntity<>("Service not found", HttpStatus.NOT_FOUND);
        }
        ServiceInfo serviceInfo = globalServices.get(serviceKey);
        serviceInfo.setBalancingEnabled(balancingEnabled);
        log.info("Updated serviceInfo: {}", serviceInfo);
        globalServices.put(serviceKey, serviceInfo);
        log.info("Updated globalServices: {}", globalServices);
        return ResponseEntity.ok("Balancing status updated");
    }
}
