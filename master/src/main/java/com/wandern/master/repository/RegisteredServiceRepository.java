package com.wandern.master.repository;

import com.wandern.master.entity.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {
    RegisteredService findByDeploymentId(String deploymentId);

}
