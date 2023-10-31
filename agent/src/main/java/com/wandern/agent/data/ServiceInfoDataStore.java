package com.wandern.agent.data;

import com.wandern.model.ServiceInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ServiceInfoDataStore {

    private final ConcurrentHashMap<String, ServiceInfo> serviceInfoMap = new ConcurrentHashMap<>();

    public void saveServiceInfo(String key, ServiceInfo serviceInfo) {
        serviceInfoMap.put(key, serviceInfo);
    }

    public ServiceInfo getServiceInfo(String key) {
        return serviceInfoMap.get(key);
    }

    public Collection<ServiceInfo> getAllServices() {
        return serviceInfoMap.values();
    }

}
