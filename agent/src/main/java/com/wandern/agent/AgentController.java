package com.wandern.agent;

import com.wandern.agent.health.ServiceHealthInfo;
import com.wandern.agent.health.ServiceHealthRegistry;
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
    private final ServiceHealthRegistry serviceHealthRegistry;

    /**
     * Регистрирует сервис в агенте и мастер-сервисе.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return Ответ с сообщением о успешной регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
        agentService.registerServiceInAgent(serviceInfoDTO);
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
        Collection<ServiceInfoDTO> allServices = agentService.getAllServices();
        return ResponseEntity.ok(allServices);
    }

    /**
     * Обновляет информацию о состоянии здоровья указанного сервиса.
     *
     * @param serviceHealthInfo информация о состоянии здоровья сервиса.
     * @return Ответ без содержимого.
     */
    @Deprecated // крч пока хз
    @PostMapping("/update-service-health")
    public ResponseEntity<Void> updateServiceHealth(@RequestBody ServiceHealthInfo serviceHealthInfo) {
        serviceHealthRegistry.updateServiceHealth(serviceHealthInfo.serviceName(), serviceHealthInfo.healthStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает мапу состояния здоровья всех зарегистрированных сервисов.
     *
     * @return Ответ с картой состояния здоровья сервисов.
     */
    @GetMapping("/services-health")
    public ResponseEntity<Map<String, ServiceHealthInfo>> getServicesHealth() {
        return ResponseEntity.ok(serviceHealthRegistry.getAllServiceHealthInfo());
    }
}
