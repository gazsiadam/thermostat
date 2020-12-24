package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.controller.UiWebsocketController;
import com.sh.thermostat.actuator.exception.TemperatureSensorNotFunctionalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TemperatureService {

    private static final long ONE_MINT_IN_MILLISECONDS = 60 * 1000;

    private final PiService piService;
    private final SystemService systemService;
    private final UiWebsocketController websocketController;
    private double lastMeasuredTemperature;

    @Scheduled(fixedRate = ONE_MINT_IN_MILLISECONDS, initialDelay = ONE_MINT_IN_MILLISECONDS)
    public void measureTemperature() {
        try {
            if (!systemService.getSystemStatus().isTemperatureSensorMalfunction()) {
                lastMeasuredTemperature = this.piService.getTemperature();
            }
        } catch (Throwable t) {
            log.error("Cannot check room temperature. Setting temperature malfunction to TRUE", t);
            lastMeasuredTemperature = -100;
            systemService.setTemperatureSensorMalfunction();
        } finally {
            websocketController.sendTemperature(lastMeasuredTemperature);
        }
    }

    public double getTemperature() {
        return lastMeasuredTemperature;
    }

    public double getSensorTemperature() throws TemperatureSensorNotFunctionalException {
        lastMeasuredTemperature = piService.getTemperature();
        return lastMeasuredTemperature;
    }
}
