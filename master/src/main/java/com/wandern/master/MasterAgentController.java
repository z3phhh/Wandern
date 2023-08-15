package com.wandern.master;

import com.wandern.agent.ServiceInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class MasterAgentController {

    private final Map<String, ServiceInfo> globalServices = new ConcurrentHashMap<>();

    @PostMapping("/update")
    public ResponseEntity<String> updateServices(@RequestBody Map<String, ServiceInfo> services) {
        globalServices.putAll(services);
        return ResponseEntity.ok("Services updated");
    }
}