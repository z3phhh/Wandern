package com.wandern.master.repository;

import com.wandern.master.entity.MetricsEntity;
import com.wandern.master.entity.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricsRepository extends JpaRepository<MetricsEntity, Long> {
    Optional<MetricsEntity> findByRegisteredService(RegisteredService registeredService);
}
