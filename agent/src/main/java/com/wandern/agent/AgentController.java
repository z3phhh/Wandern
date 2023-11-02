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

}
