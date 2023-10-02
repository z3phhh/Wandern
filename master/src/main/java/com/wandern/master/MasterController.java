package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceStatusDTO;
import com.wandern.master.DTO.ServiceDetailsDTO;
import com.wandern.master.DTO.projection.ServiceDetailsProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер-маршрутизатор, предоставляющий API для раьоты с мастер-сервисом.
 * Отвечает за регистрацию сервисов, сохранение метриу и обновление статуса сервисов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MasterController {

    private final MasterService masterService;
    private final MasterJDBCDataAccessService dataAccessService;

//    private final RegisteredServiceRepository registeredServiceRepository;//dele

    /**
     * Регистрирует сервис в глобальной топологии.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return ответ о результате регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody com.wandern.clients.ServiceInfoDTO serviceInfoDTO) {
        return masterService.registerService(serviceInfoDTO);
    }

    /**
     * Сохраняет метрики для указанного сервиса.
     *
     * @param deploymentId идентификатор развертывания сервиса.
     * @param metricsDTO метрики сервиса.
     * @return ответ о результате сохранения метрик.
     */
    @PostMapping("/{deploymentId}/metrics")
    public ResponseEntity<String> saveMetrics(@PathVariable String deploymentId,
                                              @RequestBody MetricsDTO metricsDTO) {
        return masterService.saveMetrics(deploymentId, metricsDTO);
    }

    @PostMapping("/status/update")
    public ResponseEntity<Void> updateServiceStatus(@RequestBody ServiceStatusDTO statusUpdate) {
        masterService.updateServiceStatus(statusUpdate);
        return ResponseEntity.ok().build();
    }

/*
    @GetMapping
    public List<ServiceDetailsDTO> getAllServiceDetails() {
        return masterService.getAllServiceDetails();
    }
*/


    // with jdbc template
    @GetMapping
    public List<ServiceDetailsProjection> getAllServiceDetails() {
        return dataAccessService.selectAllServiceDetails();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDetailsProjection> getServiceDetailsById(@PathVariable Long id) {
        return dataAccessService.selectServiceDetailsById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}