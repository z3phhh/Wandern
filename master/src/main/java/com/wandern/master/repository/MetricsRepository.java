package com.wandern.master.repository;

import com.wandern.master.entity.Metrics;
import com.wandern.master.entity.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MetricsRepository extends JpaRepository<Metrics, Long> {
    Optional<Metrics> findByRegisteredService(RegisteredService registeredService);
}
