//package com.wandern.arm;
//
//
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/arm")
//@Slf4j
//@AllArgsConstructor
//public class ArmController {
//
//    private final ArmService armService;
//
//
//    @PostMapping("/updateTopology")
//    public ResponseEntity<String> updateTopology(@RequestBody Map<String, ServiceInfo> topologyUpdates) {
//        log.info("Received topology update request: {}", topologyUpdates);
//        ServiceResponse<String> serviceResponse = armService.updateTopology(topologyUpdates);
//        return new ResponseEntity<>(serviceResponse.body(), serviceResponse.status());
//    }
//
//    @GetMapping("/getTopology")
//    public ResponseEntity<Map<String, ServiceInfo>> getTopology() {
//        log.info("Received get topology request");
//        ServiceResponse<Map<String, ServiceInfo>> serviceResponse = armService.getTopology();
//        return new ResponseEntity<>(serviceResponse.body(), serviceResponse.status());
//    }
//
//    @PostMapping("/updateBalancingStatus")
//    public ResponseEntity<String> updateBalancingStatus(
//            @RequestParam String deploymentId,
//            @RequestParam boolean balancingEnabled) {
//        return armService.updateBalancingStatus(deploymentId, balancingEnabled);
//    }
//
//}