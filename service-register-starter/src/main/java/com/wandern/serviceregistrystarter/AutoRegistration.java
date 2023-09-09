package com.wandern.serviceregistrystarter;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AutoRegistration {

    private static final Logger logger = LoggerFactory.getLogger(AutoRegistration.class);

    private final RegistrationService registrationService;

    @EventListener(ApplicationReadyEvent.class)
    public void registerService() {
        logger.info("Service registration triggered.");
        registrationService.registerAtStartup();
    }
}
