package com.wandern.master.repository;

import com.wandern.master.DTO.projection.ServiceDetailsProjection;
import com.wandern.master.entity.RegisteredService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {
    Optional<RegisteredService> findByDeploymentId(String deploymentId);
//    List<ServiceDetailsProjection> findAllBy();

    void deleteByDeploymentId(String deploymentId);

}
