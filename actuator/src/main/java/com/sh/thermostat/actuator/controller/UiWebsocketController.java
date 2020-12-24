package com.sh.thermostat.actuator.controller;

import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import com.sh.thermostat.actuator.domain.thermostat.Program;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Log4j2
@Controller
@RequiredArgsConstructor
public class UiWebsocketController {

    public final static String TEMPERATURE_TOPIC = "/temperature";
    public final static String ACTIVE_PROGRAM_TOPIC = "/active-program";
    public final static String SYSTEM_HEALTH_TOPIC = "/active-program";
    public final static String LOG_TOPIC = "/log";

    private final SimpMessagingTemplate template;

    public void sendTemperature(Double temperature) {
        template.convertAndSend(TEMPERATURE_TOPIC, temperature);
    }

    public void sendActiveProgram(Program activeProgram) {
        template.convertAndSend(ACTIVE_PROGRAM_TOPIC, activeProgram);
    }

    public void sendSystemHealth(String health) {
        template.convertAndSend(SYSTEM_HEALTH_TOPIC, health);
    }

    public void sendLog(ThermostatLog log) {
        template.convertAndSend(LOG_TOPIC, log.toString());
    }

}
