package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.master.dto.projection.ServiceDetailsProjection;
import com.wandern.model.ServiceInfo;
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
@CrossOrigin
public class MasterController {

    private final MasterService masterService;
    private final MasterJDBCDataAccessService dataAccessService;

    /**
     * Регистрирует сервис в глобальной топологии.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return ответ о результате регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<Void> registerService(@RequestBody ServiceInfo serviceInfo) {
        masterService.registerService(serviceInfo);
        return ResponseEntity.ok().build();
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

/*    @PostMapping("/status/update")
    public ResponseEntity<Void> updateServiceStatus(@RequestBody ServiceStatusDTO statusUpdate) {
        masterService.updateServiceStatus(statusUpdate);
        return ResponseEntity.ok().build();
    }*/

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

/*    @GetMapping("/set-down/{deploymentId}")
    public ResponseEntity<Void> setServiceDown(@PathVariable String deploymentId) {
        var statusUpdate = new ServiceStatusDTO(deploymentId, "DOWN");
        masterService.updateServiceStatus(statusUpdate);
        return ResponseEntity.ok().build();
    }*/
}