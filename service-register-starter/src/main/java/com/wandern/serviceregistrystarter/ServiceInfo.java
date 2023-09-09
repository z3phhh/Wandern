package com.wandern.serviceregistrystarter;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

@Data
@Component
public class ServiceInfo implements ApplicationContextAware {

//    private String id = UUID.randomUUID().toString();
    private String deploymentId = UUID.randomUUID().toString();;
    private String system;
    private String serviceUrl;

    @Value("${server.port}")
    private int port;

    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            this.serviceUrl = "http://" + ip + ":" + port;

            String[] beanNames = applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class);
            if (beanNames != null && beanNames.length > 0) {
                Object applicationBean = applicationContext.getBean(beanNames[0]);
                if (applicationBean != null) {
                    system = applicationBean.getClass().getSimpleName().split("\\$\\$")[0];
                }
            }

            deploymentId = UUID.randomUUID().toString();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}

