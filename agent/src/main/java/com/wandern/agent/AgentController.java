package com.wandern.agent;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@AllArgsConstructor
@RestController
public class AgentController {

    private final AgentService agentService;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfo serviceInfo) {
        return agentService.registerService(serviceInfo);
    }

    @GetMapping("/getLocalTopology")
    public ResponseEntity<Map<String, ServiceInfo>> getLocalTopology() {
        Map<String, ServiceInfo> localTopology = agentService.getLocalTopology();
        return ResponseEntity.ok(localTopology);
    }


}
