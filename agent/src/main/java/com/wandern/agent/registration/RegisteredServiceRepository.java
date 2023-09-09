package com.wandern.agent.registration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredServiceRepository extends JpaRepository<RegisteredService, Long> {
}
