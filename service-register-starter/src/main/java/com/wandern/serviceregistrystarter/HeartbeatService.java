package com.wandern.serviceregistrystarter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HeartbeatService {

    @Value("${service-registry.url}")
    private String serverUrl; // take out from here in conf

    @Autowired
    private RestTemplate restTemplate; // take out from here in conf

    @Scheduled(fixedRate = 5000) // every 5 sec repeat
    public void sendHeartbeat() {
        String heartbeatUrl = serverUrl + "/heartbeat";
        ResponseEntity<String> response = restTemplate.postForEntity(heartbeatUrl, null, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            System.out.println("Heartbeat successfully sent"); // en
        } else {
            System.out.println("Problem with sending a heartbeat"); // en
        }
    }
}

