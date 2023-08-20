package com.wandern.master;

import com.wandern.agent.ServiceInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@AllArgsConstructor
public class MasterController {

    private final MasterService masterService;

    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfo serviceInfo) {
        String result = masterService.registerService(serviceInfo);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/updateTopology")
    public ResponseEntity<String> updateTopology(@RequestBody Map<String, ServiceInfo> topologyUpdates) {
        masterService.updateTopology(topologyUpdates);
        return ResponseEntity.ok("Topology updated");
    }

    @GetMapping("/getTopology")
    public ResponseEntity<Map<String, ServiceInfo>> getTopology() {
        return ResponseEntity.ok(masterService.getTopology());
    }

    @PostMapping("/updateBalancingStatus")
    public ResponseEntity<String> updateBalancingStatus(@RequestBody Map<String, Object> requestBody) {
        return masterService.updateBalancingStatus(requestBody);
    }

    @PostMapping("/updateHealthStatus")
    public ResponseEntity<String> updateHealthStatus(@RequestBody Map<String, Boolean> healthStatusUpdates) {
        masterService.updateHealthStatus(healthStatusUpdates);
        return ResponseEntity.ok("Health status updated");
    }

}