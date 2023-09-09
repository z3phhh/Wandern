package com.wandern.serviceregistrystarter;

import com.wandern.serviceregistrystarter.metrics.MetricsCollector;
import com.wandern.serviceregistrystarter.metrics.MetricsController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RegistrationAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ServiceInfo serviceInfo() {
        return new ServiceInfo();
    }

    @Bean
    public AutoRegistration autoRegistration(RegistrationService registrationService) {
        return new AutoRegistration(registrationService);
    }

    @Bean
    public MetricsCollector metricsCollector() {
        return new MetricsCollector();
    }

    @Bean
    public DTOMapper dtoMapper() {
        return new DTOMapper();
    }


    @Bean
    public RegistrationService registrationService(RestTemplate restTemplate,
                                                   ServiceInfo serviceInfo,
                                                   DTOMapper dtoMapper) {
        return new RegistrationService(restTemplate, serviceInfo, dtoMapper);
    }

    // remove if there is no metrics package
    @Bean
    public MetricsController metricsController(MetricsCollector metricsCollector, DTOMapper dtoMapper, ServiceInfo serviceInfo) {
        return new MetricsController(metricsCollector, dtoMapper, serviceInfo);
    }

    @Bean
    public String helloStarterBean() {
        System.out.println("Hello from the custom starter!!!");
        return "helloStarter";
    }
}
