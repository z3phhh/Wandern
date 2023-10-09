package com.wandern.starter;

import com.wandern.clients.ServiceInfoDTO;
import com.wandern.clients.ServiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.net.*;
import java.util.Enumeration;
import java.util.UUID;

// TODO: MicroserviceCollectorInfo
@Component
public class ServiceInfoCollector implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ServiceInfoCollector.class);

    private ServiceInfoDTO serviceInfoDTO;
    private ApplicationContext applicationContext;

    @Value("${server.port}")
    private int port;

    @Value("${deployment.prefix:#{null}}")
    private String deploymentPrefix;

    @Value("${server.servlet.context-path:${default.context-path:/ctx}}")
    private String contextPath;

    private String generateShortUUID(int length) {
        return UUID.randomUUID().toString().replace("-", "").substring(0, length);
    }

    public ServiceInfoDTO collectServiceInfo() {
        try {
            String ip = getExternalIpAddress();
            String serviceUrl = "http://" + ip + ":" + port;
            String deploymentUnit = generateShortUUID(12);
            String shortUUIDForDeploymentId = deploymentUnit.substring(0, 4);
            String deploymentId = (deploymentPrefix != null ? deploymentPrefix : getMainClassName()) + "-" + shortUUIDForDeploymentId;
            String system = getMainClassName();

            return new ServiceInfoDTO(
                    deploymentId,
                    deploymentUnit,
                    system,
                    serviceUrl,
                    contextPath,
                    port,
                    ip,
                    ServiceStatus.UP
            );

        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMainClassName() {
        String[] beanNames = applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class);
        if (beanNames.length > 0) {
            Object applicationBean = applicationContext.getBean(beanNames[0]);
            return applicationBean.getClass().getSimpleName().split("\\$\\$")[0];
        }
        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private String getExternalIpAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface iface = interfaces.nextElement();
            if (iface.isLoopback() || !iface.isUp()) {
                continue;
            }

            // проверка на имя сетевого интерфейса
            if (!"enp9s0".equals(iface.getName())) {
                continue;
            }

            Enumeration<InetAddress> addresses = iface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return addr.getHostAddress();
                }
            }
        }
        return null;
    }

    public ServiceInfoDTO getServiceInfoData() {
        return serviceInfoDTO;
    }


}


