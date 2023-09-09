package com.wandern.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/services")
public class ServiceInfoController {

    @Autowired
    private ServiceInfoService service;

    @PostMapping
    public ResponseEntity<ServiceInfo> registerService(@RequestBody ServiceInfo serviceInfo) {
        ServiceInfo savedServiceInfo = service.registerService(serviceInfo);
        return new ResponseEntity<>(savedServiceInfo, HttpStatus.CREATED);
    }

    // Add other CRUD endpoints as required
}
