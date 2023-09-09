package com.wandern.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ServiceInfoService {
    @Autowired
    private ServiceInfoRepository repository;

    public ServiceInfo registerService(ServiceInfo serviceInfo) {
        return repository.save(serviceInfo);
    }

    public Optional<ServiceInfo> findById(Long id) {
        return repository.findById(id);
    }

    // Add other CRUD operations as required
}
