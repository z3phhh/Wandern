package com.wandern.starter;

import com.wandern.starter.health.DefaultHealthCheck;
import com.wandern.starter.health.HealthCheck;
//import com.wandern.starter.health.HealthCheckAggregator;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.List;

// TODO: возможно что-то надо поудалять
@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

/*    @Bean
    public HealthCheckAggregator healthCheckAggregator(List<HealthCheck> healthChecks) {
        return new HealthCheckAggregator(healthChecks);
    }*/
    @Bean
    public DefaultHealthCheck defaultHealthCheck() {
        return new DefaultHealthCheck();
    }

    @Bean
    public MetricsAndInfoController discoveryController(RestTemplate restTemplate,
                                                        ServiceInfoCollector serviceInfoCollector,
                                                        MetricsCollector metricsCollector) {
        return new MetricsAndInfoController(restTemplate, serviceInfoCollector, metricsCollector);
    }

    @Bean
    public ServiceInfoCollector serviceInfo() {
        return new ServiceInfoCollector();
    }

    @Bean
    public MetricsCollector metricsCollector() {
        return new MetricsCollector();
    }

    @Bean
    public String helloStarterBean() {
        System.out.println("Hello from the custonm starter!!!");
        return "helloStarter";
    }
}
