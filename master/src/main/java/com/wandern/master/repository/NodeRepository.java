package com.wandern.master.repository;

import com.wandern.master.entity.Node;
import com.wandern.master.entity.NodeMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NodeRepository extends JpaRepository<Node, Long> {

    Optional<Node> findByNodeId(String nodeId);
    Optional<Node> findByNodeIp(String nodeIp);
}