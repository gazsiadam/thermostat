package com.sh.thermostat.actuator.listener;

import com.sh.thermostat.actuator.pi.PiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class StartupListener implements ApplicationListener<ContextStartedEvent> {

    private final PiController piController;

    @Override
    public void onApplicationEvent(ContextStartedEvent contextClosedEvent) {
        log.info("Startup signal received. Running startup script...");
        piController.stopHeating();
        log.trace("Startup script - turn heating off");
    }
}
