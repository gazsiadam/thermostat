package com.sh.thermostat.actuator.controller;

import com.sh.thermostat.actuator.domain.thermostat.Program;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import com.sh.thermostat.actuator.domain.thermostat.ThermostatStatus;
import com.sh.thermostat.actuator.exception.TemperatureSensorNotFunctionalException;
import com.sh.thermostat.actuator.service.PiService;
import com.sh.thermostat.actuator.service.ProgramService;
import com.sh.thermostat.actuator.service.TemperatureService;
import com.sh.thermostat.actuator.service.ThermostatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RequiredArgsConstructor
@RestController("/api/thermostat")
public class ThermostatRestController {

    private final ThermostatService thermostatService;
    private final ProgramService programService;
    private final TemperatureService temperatureService;
    private final PiService piService;

    @GetMapping("/set-manual-mode")
    public void setManualMode(@RequestParam("manualTemperature") Double manualTemperature) {
        programService.setManualTemperature(manualTemperature);
    }

    @GetMapping("/clear-manual-mode")
    public void clearManualMode() {
        programService.clearManualMode();
    }

    @GetMapping("/program-schedule")
    public ProgramSchedule getProgramSchedule() {
        return programService.getProgramSchedule();
    }

    @PostMapping("/program-schedule")
    public void setProgramSchedule(@RequestBody ProgramSchedule programSchedule) {
        programService.setProgramSchedule(programSchedule);
    }

    @GetMapping("/active-program")
    public Program getActiveProgram() {
        return programService.getActiveProgram();
    }

    @GetMapping("/sensor-temperature")
    public double getSensorTemperature() throws TemperatureSensorNotFunctionalException {
        return temperatureService.getSensorTemperature();
    }

    @GetMapping("/last-measured-temperature")
    public double getLastMeasuredTemperature() {
        return temperatureService.getTemperature();
    }

    @GetMapping("/programmed-temperature")
    public double getProgrammedTemperature() {
        return programService.getActiveProgram().getTemperature();
    }

    @GetMapping("/status")
    public ThermostatStatus getThermostatStatus() {
        return thermostatService.getThermostatStatus();
    }

    @GetMapping("/turn-heating-on")
    public void turnHeatingOn() {
        piService.startHeating();
    }

    @GetMapping("/turn-heating-off")
    public void turnHeatingOff() {
        piService.stopHeating();
    }

}
