package com.wandern.master.repository;

import com.wandern.master.entity.HealthMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthMetricsRepository extends JpaRepository<HealthMetrics, Long> {
}