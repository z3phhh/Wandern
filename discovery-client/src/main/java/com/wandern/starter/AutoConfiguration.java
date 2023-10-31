package com.wandern.starter;

import com.wandern.starter.health.DefaultHealthCheck;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebClient webClient() {
        return WebClient.create();
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public DefaultHealthCheck defaultHealthCheck() {
        return new DefaultHealthCheck();
    }

    @Bean
    public MetricsAndInfoController discoveryController(RestTemplate restTemplate,
                                                        ServiceInfoCollector serviceInfoCollector,
                                                        MetricsCollector metricsCollector,
                                                        ApplicationContext appContext) {

        return new MetricsAndInfoController(
                restTemplate,
                serviceInfoCollector,
                metricsCollector,
                appContext);
    }

    @Bean
    public ServiceInfoCollector serviceInfo() {
        return new ServiceInfoCollector();
    }

    @Bean
    public MetricsCollector metricsCollector() {
        return new MetricsCollector();
    }
}
