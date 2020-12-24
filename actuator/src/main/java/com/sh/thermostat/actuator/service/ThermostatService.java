package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.thermostat.ActivationRange;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import com.sh.thermostat.actuator.domain.thermostat.ThermostatStatus;
import com.sh.thermostat.actuator.service.weather.OutsideTemperatureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Log4j2
@Service
@RequiredArgsConstructor
public class ThermostatService {

    private static final long INTERVAL_CHECK_TEMPERATURE = 90 * 1000; // 1.5 MINUTE

    private final DecimalFormat df = new DecimalFormat("#.##");

    private final PiService piService;
    private final SystemService systemService;
    private final ProgramService programService;
    private final DateTimeService dateTimeService;
    private final TemperatureService temperatureService;
    private final OutsideTemperatureService outsideTemperatureService;

    @Scheduled(fixedRate = INTERVAL_CHECK_TEMPERATURE, initialDelay = INTERVAL_CHECK_TEMPERATURE)
    public void heatingProcess() {
        log.debug("Verify if temperature sensor working properly...");
        if (systemService.getSystemStatus().isTemperatureSensorMalfunction()) {
            log.warn("Temperature sensor malfunction. Heater is will not be activated.");
            piService.stopHeating();
            return;
        }

        log.debug("Verify temperature and start heating if necessary...");
        ProgramSchedule programSchedule = this.programService.getProgramSchedule();
        if (!programSchedule.isHeatingEnabled()) {
            log.warn("Heating functionality turned off. Heater is not activated.");
            piService.stopHeating();
            return;
        }

        double programmedTemp;
        if (programSchedule.isManualMode()) {
            programmedTemp = programSchedule.getManualModeTemperature();
        } else {
            programmedTemp = this.programService.getActiveProgram().getTemperature();
        }

        // logic to start/stop heating based on program and sensor temperature
        manageHeating(programmedTemp, temperatureService.getTemperature(), programSchedule.getActivationRange());
    }

    private void manageHeating(Double programmedTemp, Double sensorTemperature, ActivationRange activationRange) {
        double tempDifference = Math.abs(programmedTemp - sensorTemperature);
        switch (sensorTemperature.compareTo(programmedTemp)) {
            case -1: { // sensorTemp < programmedTemp
                if (tempDifference > activationRange.getLowerDifference()) {
                    log.debug("Start heating...");
                    piService.startHeating();
                }
                break;
            }
            case 1: { // sensorTemp > programmedTemp
                if (tempDifference > activationRange.getUpperDifference()) {
                    log.debug("Stop heating...");
                    piService.stopHeating();
                }
                break;
            }
        }
    }

    public ThermostatStatus getThermostatStatus() {
        return ThermostatStatus.builder()
                .lastMeasuredTemperature(Double.valueOf(df.format(temperatureService.getTemperature())))
                .lastMeasuredOutsideTemperature(Double.valueOf(df.format(outsideTemperatureService.getLastOutsideTemperature())))
                .programSchedule(programService.getProgramSchedule())
                .activeProgram(programService.getActiveProgram())
                .isHeatingOn(piService.isHeatingOn())
                .time(dateTimeService.nowLocalDateTime())
                .systemStatus(systemService.getSystemStatus())
                .build();
    }

}
