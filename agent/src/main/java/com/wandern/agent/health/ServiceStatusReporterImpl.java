package com.wandern.agent.health;

import com.wandern.agent.AgentService;
import com.wandern.clients.ServiceStatusDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Component
public class ServiceStatusReporterImpl implements ServiceStatusReporter {

    private static final Logger logger = LoggerFactory.getLogger(AgentService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${master.url}")
    private String masterServiceUrl;

    @Override
    public void sendStatusToMaster(String deploymentId, Status status) {
        String targetUrl = masterServiceUrl + "/master/api/v1/services/status/update";
        ServiceStatusDTO serviceStatus = new ServiceStatusDTO(deploymentId, status.toString());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ServiceStatusDTO> request = new HttpEntity<>(serviceStatus, headers);
        try {
            restTemplate.postForEntity(targetUrl, request, Void.class);
        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            logger.error("Failed to send status to master for deploymentId {}. Response: {}", deploymentId, httpEx.getResponseBodyAsString(), httpEx);
        } catch (ResourceAccessException resourceEx) {
            logger.warn("Failed to access master server at {}. Might be down or network issues.", targetUrl /*, resourceEx*/);
        } catch (Exception e) {
            logger.error("Unexpected error occurred while sending status to master for deploymentId {}", deploymentId, e);
        }
    }

}