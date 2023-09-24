package com.wandern.agent.health;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class HealthStatusSender {

    @Value("${master.service.url}")
    private String masterServiceUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendStatusToMaster(String deploymentId, Status status) {
        String targetUrl = masterServiceUrl + "/master/api/v1/services/status/update";

        ServiceStatusDTO serviceStatus = new ServiceStatusDTO(deploymentId, status.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ServiceStatusDTO> request = new HttpEntity<>(serviceStatus, headers);

        try {
            restTemplate.postForEntity(targetUrl, request, Void.class);
        } catch (Exception e) {
            // Обработка ошибок при отправке данных в мастер
        }
    }

    @Data
    @AllArgsConstructor
    public static class ServiceStatusDTO {
        private String deploymentId;
        private String status;
    }
}
