package com.wandern.serviceregistrystarter;

//import com.wandern.serviceregistrystarter.health.HealthCheck;
//import com.wandern.serviceregistrystarter.health.HealthCheckAggregator;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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

//    @Bean
//    public HealthCheckAggregator healthCheckAggregator(List<HealthCheck> healthChecks) {
//        return new HealthCheckAggregator(healthChecks);
//    }

    @Bean
    public ServiceMapper serviceMapper() {
        return Mappers.getMapper(ServiceMapper.class);
    }

    @Bean
    public MetricsAndInfoController discoveryController(RestTemplate restTemplate,
                                                        ServiceInfoCollector serviceInfoCollector,
                                                        ServiceMapper serviceMapper,
                                                        MetricsCollector metricsCollector) {
        return new MetricsAndInfoController(restTemplate, serviceInfoCollector, serviceMapper, metricsCollector);
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
        System.out.println("Hello from the custom starter!!!");
        return "helloStarter";
    }
}
