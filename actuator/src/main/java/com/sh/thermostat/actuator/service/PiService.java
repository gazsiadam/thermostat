package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.log.LogType;
import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import com.sh.thermostat.actuator.exception.TemperatureSensorNotFunctionalException;
import com.sh.thermostat.actuator.pi.PiController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PiService {

    private final DateTimeService dateTimeService;
    private final PiController piController;
    private final LogService logService;

    @Value("${thermostat.temperature.sensor.callibration:0}")
    private double temperatureSensorCalibration;

    public Double getTemperature() throws TemperatureSensorNotFunctionalException {
        int retries = 0;
        List<Double> measuredTemperatures = new ArrayList<>();
        while (measuredTemperatures.size() < 3 && retries < 20) {
            double temp = piController.getSensorTemperature();
            if (temp > Double.MIN_VALUE) {
                measuredTemperatures.add(temp + temperatureSensorCalibration);
            }
            retries++;
        }

        if (measuredTemperatures.size() < 3) {
            log.error("Temperature sensor is not responding! Cannot measure room temperature");
            logService.log(ThermostatLog.builder()
                    .date(dateTimeService.nowLocalDateTime())
                    .logType(LogType.SYSTEM_EVENT)
                    .data("Temperature sensor is not responding!")
                    .build());
            throw new TemperatureSensorNotFunctionalException();
        }

        double averageTemp = measuredTemperatures.stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(Double.MIN_VALUE);// should not happen

        log.debug("Temperature measured: " + measuredTemperatures + ", average: " + averageTemp);
        logService.logInsideTemperature(averageTemp);
        return averageTemp;
    }

    public Boolean isHeatingOn() {
        return piController.isHeatingOn();
    }

    public void startHeating() {
        if (isHeatingOn()) {
            log.debug("Heating already turned on...");
            return;
        }
        piController.startHeating();
        logService.logStartHeating();
    }

    public void stopHeating() {
        if (!isHeatingOn()) {
            log.debug("Heating already turned off...");
            return;
        }
        piController.stopHeating();
        logService.logStopHeating();
    }

}
