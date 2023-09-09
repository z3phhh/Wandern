package com.wandern.arm;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ArmService {

    private final RestTemplate restTemplate;
    private final String masterServiceUrl;

    public ServiceResponse<String> updateTopology(Map<String, ServiceInfo> topologyUpdates) {
        try {
            ResponseEntity<String> response = restTemplate
                    .postForEntity(masterServiceUrl + "/updateTopology", topologyUpdates, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Topology updated successfully");
                return new ServiceResponse<>("Topology updated successfully", HttpStatus.OK);
            } else {
                log.warn("Topology update failed, status code: {}", response.getStatusCode());
                return new ServiceResponse<>("Topology update failed", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error updating topology", e);
            return new ServiceResponse<>("Error updating topology", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ServiceResponse<Map<String, ServiceInfo>> getTopology() {
        log.info("Fetching topology from master");
        try {
            ResponseEntity<Map> response = restTemplate
                    .getForEntity(masterServiceUrl + "/getTopology", Map.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Topology fetched successfully");
                return new ServiceResponse<>(response.getBody(), HttpStatus.OK);
            } else {
                log.warn("Topology fetch failed, status code: {}", response.getStatusCode());
                return new ServiceResponse<>(null, response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error fetching topology", e);
            return new ServiceResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<String> updateBalancingStatus(String deploymentId, boolean balancingEnabled) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            JSONObject requestBody = new JSONObject();
            requestBody.put("deploymentId", deploymentId);
            requestBody.put("balancingEnabled", balancingEnabled);
            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
            ResponseEntity<String> masterResponse = restTemplate
                    .postForEntity(masterServiceUrl + "/updateBalancingStatus", request, String.class);
            if (masterResponse.getStatusCode().is2xxSuccessful()) {
                log.info("Balancing status updated successfully in master");
                return new ResponseEntity<>("Balancing status updated successfully", HttpStatus.OK);
            } else {
                log.warn("Balancing status update in master failed, status code: {}", masterResponse.getStatusCode());
                return new ResponseEntity<>("Balancing status update in master failed", masterResponse.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error updating balancing status", e);
            return new ResponseEntity<>("Error updating balancing status", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

