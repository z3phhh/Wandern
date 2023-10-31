package com.wandern.master.repository;

import com.wandern.master.entity.Node;
import com.wandern.master.entity.NodeMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NodeMetricsRepository extends JpaRepository<NodeMetrics, Long> {
    Optional<NodeMetrics> findByNode(Node node);
}