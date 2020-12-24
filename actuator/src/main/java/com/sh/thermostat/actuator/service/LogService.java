package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.controller.UiWebsocketController;
import com.sh.thermostat.actuator.domain.log.LogType;
import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import com.sh.thermostat.actuator.repository.log.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogService {

    private final UiWebsocketController uiWebsocketController;
    private final DateTimeService dateTimeService;
    private final LogRepository logRepository;

    public void logStartHeating() {
        persistLog(ThermostatLog.builder()
                .logType(LogType.ACTION_START_HEATING)
                .date(dateTimeService.nowLocalDateTime())
                .build());
    }

    public void logStopHeating() {
        persistLog(ThermostatLog.builder()
                .logType(LogType.ACTION_STOP_HEATING)
                .date(dateTimeService.nowLocalDateTime())
                .build());
    }

    public void logOutsideTemperature(Double outsideTemperature) {
        persistLog(ThermostatLog.builder()
                .logType(LogType.TEMPERATURE_OUTSIDE)
                .data(outsideTemperature.toString())
                .date(dateTimeService.nowLocalDateTime())
                .build());
    }

    public void logInsideTemperature(Double insideTemperature) {
        persistLog(ThermostatLog.builder()
                .logType(LogType.TEMPERATURE_INSIDE)
                .data(insideTemperature.toString())
                .date(dateTimeService.nowLocalDateTime())
                .build());
    }

    public void log(ThermostatLog log) {
        persistLog(log);
    }

    private void persistLog(ThermostatLog thermostatLog) {
        uiWebsocketController.sendLog(thermostatLog);
        CompletableFuture.runAsync(() -> logRepository.persistLog(thermostatLog))
                .exceptionally(ex -> {
                    log.error(ex.getMessage());
                    return null;
                });
    }

}
