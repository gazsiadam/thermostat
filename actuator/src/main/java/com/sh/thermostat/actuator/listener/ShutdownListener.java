package com.sh.thermostat.actuator.listener;

import com.pi4j.io.gpio.GpioFactory;
import com.sh.thermostat.actuator.pi.PiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
class ShutdownListener implements ApplicationListener<ContextClosedEvent> {

    private final PiController piController;

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        log.debug("Shutdown signal received. Running shutdown script...");
        piController.stopHeating();
        log.trace("Shutdown script - turn heating off");
        GpioFactory.getInstance().shutdown();
    }
}
