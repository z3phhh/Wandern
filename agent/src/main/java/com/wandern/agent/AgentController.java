package com.wandern.agent;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
@CrossOrigin
public class AgentController {

    private final AgentService agentService;
    @PostMapping("/register")
    public void registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        agentService.registerService(serviceInfoDTO);
    }

    @GetMapping("/services")
    public List<ServiceInfoDTO> getAllRegisteredServices() {
        return agentService.getAllServices();
    }

    @GetMapping("/metrics")
    public Map<String, MetricsDTO> getAllServiceMetrics() {
        return agentService.getAllServiceMetrics();
    }

/*    @GetMapping("/statuses")
    public Map<String, LivenessStatus> getAllServiceStatuses() {
        return healthCheckService.getAllServiceStatuses();
    }

    @GetMapping("/status")
    public LivenessStatus getServiceStatus(@RequestParam String serviceEndpoint) {
        return healthCheckService.getServiceStatus(serviceEndpoint);
    }*/


    /////////////////////
/*    @PostMapping("/register")
    public ResponseEntity<String> registerAndMonitorService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        agentService.registerServiceInAgent(serviceInfoDTO);
        healthCheckAgent.startMonitoringService(serviceInfoDTO);
        agentService.registerServiceInMaster(serviceInfoDTO);
        return ResponseEntity.ok("Service registered successfully in agent and master.");
    }*/

    /**
     * Возвращает список всех зарегистрированных сервисов.
     *
     * @return Ответ с коллекцией зарегистрированных сервисов.
     */
/*    @GetMapping("/services")
    public ResponseEntity<Collection<ServiceInfoDTO>> getAllRegisteredServices() {
        return ResponseEntity.ok(serviceRegistry.getAllServices());
    }*/

    /**
     * Возвращает мапу состояния здоровья всех зарегистрированных сервисов.
     *
     * @return Ответ с картой состояния здоровья сервисов.
     */
/*    @GetMapping("/services-health") // not work
    public ResponseEntity<Map<String, HealthInfo>> getServicesHealth() {
        return ResponseEntity.ok(serviceHealthRegistry.getAllServiceHealthInfo());
    }*/
}
