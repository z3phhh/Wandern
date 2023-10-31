package com.wandern.agent.data;

import com.wandern.agent.health.LivenessStatus;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LivenessDataStore {

    private final ConcurrentHashMap<String, LivenessStatus> livenessResults = new ConcurrentHashMap<>();

    public void saveLivenessStatus(String deploymentId, LivenessStatus status) {
        livenessResults.put(deploymentId, status);
    }

    public LivenessStatus getLivenessStatus(String deploymentId) {
        return livenessResults.get(deploymentId);
    }

    public Map<String, LivenessStatus> getAllLivenessStatuses() {
        return Collections.unmodifiableMap(livenessResults);
    }
}