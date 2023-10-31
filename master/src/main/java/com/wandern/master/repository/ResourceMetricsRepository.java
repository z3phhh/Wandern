package com.wandern.master.repository;

import com.wandern.master.entity.ResourceMetrics;
import com.wandern.master.entity.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResourceMetricsRepository extends JpaRepository<ResourceMetrics, Long> {
    Optional<ResourceMetrics> findByRegisteredService(RegisteredService registeredService);
}
