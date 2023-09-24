package com.wandern.master;

import com.wandern.clients.MetricsDTO;
import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatusDTO;
import com.wandern.master.repository.RegisteredServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

/**
 * Контроллер-маршрутизатор, предоставляющий API для раьоты с мастер-сервисом.
 * Отвечает за регистрацию сервисов, сохранение метриу и обновление статуса сервисов.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class MasterController {

    private final MasterService masterService;

    private final RegisteredServiceRepository registeredServiceRepository;

    /**
     * Регистрирует сервис в глобальной топологии.
     *
     * @param serviceInfoDTO информация о регистрируемом сервисе.
     * @return ответ о результате регистрации.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerService(@RequestBody ServiceInfoDTO serviceInfoDTO) {
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

    /**
     * Обновляет статус сервиса. Если статус "DOWN", сервис удаляется из базы данных.
     *
     * @param statusUpdate информация о статусе сервиса.
     * @return ответ о результате обновления статуса.
     */
    @Transactional
    @PostMapping("/status/update")
    public ResponseEntity<Void> updateServiceStatus(@RequestBody ServiceStatusDTO statusUpdate) {
        if ("DOWN".equals(statusUpdate.status())) {
            registeredServiceRepository.deleteByDeploymentId(statusUpdate.deploymentId());
            // + логи
        }
        return ResponseEntity.ok().build();
    }

}