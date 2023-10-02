package com.wandern.agent;

import com.wandern.agent.health.HealthCheckAgent;
import com.wandern.agent.health.model.HealthInfo;
import com.wandern.agent.registry.ServiceHealthRegistry;
import com.wandern.agent.registry.ServiceRegistry;
import com.wandern.clients.ServiceInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/agent")
public class AgentController {

    private final AgentService agentService;
    private final ServiceRegistry serviceRegistry;
    private final ServiceHealthRegistry serviceHealthRegistry;
    private final HealthCheckAgent healthCheckAgent;

    /**
     * Регистрирует сервис в агенте и мастер-сервисе.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return Ответ с сообщением о успешной регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerAndMonitorService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        agentService.registerServiceInAgent(serviceInfoDTO);
        healthCheckAgent.startMonitoringService(serviceInfoDTO);
        agentService.registerServiceInMaster(serviceInfoDTO);
        return ResponseEntity.ok("Service registered successfully in agent and master.");
    }

    /**
     * Возвращает список всех зарегистрированных сервисов.
     *
     * @return Ответ с коллекцией зарегистрированных сервисов.
     */
    @GetMapping("/services")
    public ResponseEntity<Collection<ServiceInfoDTO>> getAllRegisteredServices() {
        return ResponseEntity.ok(serviceRegistry.getAllServices());
    }

    /**
     * Возвращает мапу состояния здоровья всех зарегистрированных сервисов.
     *
     * @return Ответ с картой состояния здоровья сервисов.
     */
    @GetMapping("/services-health") // not work
    public ResponseEntity<Map<String, HealthInfo>> getServicesHealth() {
        return ResponseEntity.ok(serviceHealthRegistry.getAllServiceHealthInfo());
    }
}
