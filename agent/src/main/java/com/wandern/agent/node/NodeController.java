package com.wandern.agent.node;

import com.wandern.clients.NodeMetricsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/node")
public class NodeController {

    private final NodeService nodeService;

    @GetMapping("/metrics")
    public NodeMetricsDTO getNodeMetrics() {
        return nodeService.getNodeMetrics();
    }
}