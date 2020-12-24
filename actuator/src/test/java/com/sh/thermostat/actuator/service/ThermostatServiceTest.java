package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.system.SystemStatus;
import com.sh.thermostat.actuator.domain.thermostat.ActivationRange;
import com.sh.thermostat.actuator.domain.thermostat.Program;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test cases:
 * 1. now: 20.5d, programmed: 21d, activation range: 0.3d-0.3d, heater turned off, expected: start heating
 * 1. now: 22d, programmed: 22d, range: 0.3d-0.3d, heater turned off, expected: start heating
 * 2. now: 21.9d, programmed: 22d, range: 0.3d-0.3d, expected: start heating
 */
@Log4j2
@ExtendWith(MockitoExtension.class)
public class ThermostatServiceTest {

    public static final Double PROGRAMMED_TEMPERATURE = 21d;

    @Mock
    private ProgramService programService;

    @Mock
    private PiService piService;

    @Mock
    private TemperatureService temperatureService;

    @Mock
    private SystemService systemService;

    @InjectMocks
    private ThermostatService thermostatService;

    @DisplayName("Do not start heating if heating is turned off")
    @ParameterizedTest
    @ValueSource(doubles = {20, 21, 21.5, 22, 23})
    public void testHeatingTurnedOff(Double sensorTemperature) {
        log.info("Sensor temperature: {}", sensorTemperature);
        ActivationRange activationRange = ActivationRange.builder()
                .upperDifference(0.3d)
                .lowerDifference(0.3d)
                .build();

        ProgramSchedule programSchedule = ProgramSchedule.builder()
                .activationRange(activationRange)
                .heatingEnabled(false)
                .build();

        Mockito.when(systemService.getSystemStatus()).thenReturn(SystemStatus.builder().build());
        Mockito.when(programService.getProgramSchedule()).thenReturn(programSchedule);

        thermostatService.heatingProcess();
        Mockito.verify(piService, Mockito.never()).startHeating();
        Mockito.verify(piService, Mockito.times(1)).stopHeating();
    }

    @ParameterizedTest
    @ValueSource(doubles = {20.5, 20.4, 20.3})
    @DisplayName("Start heating when sensor temperature below programmed temp - lower limit")
    public void testStartHeating_SensorTemperatureBelowLowwerLimit(Double sensorTemperature) {
        log.info("Sensor temperature: {}", sensorTemperature);
        ActivationRange activationRange = ActivationRange.builder()
                .upperDifference(0.3d)
                .lowerDifference(0.3d)
                .build();

        Program activeProgram = Program.builder()
                .temperature(PROGRAMMED_TEMPERATURE)
                .build();

        ProgramSchedule programSchedule = ProgramSchedule.builder()
                .activationRange(activationRange)
                .heatingEnabled(true)
                .build();

        Mockito.when(systemService.getSystemStatus()).thenReturn(SystemStatus.builder().build());
        Mockito.when(programService.getActiveProgram()).thenReturn(activeProgram);
        Mockito.when(programService.getProgramSchedule()).thenReturn(programSchedule);
        Mockito.when(temperatureService.getTemperature()).thenReturn(sensorTemperature);

        thermostatService.heatingProcess();
        Mockito.verify(piService, Mockito.times(1)).startHeating();
        Mockito.verify(piService, Mockito.never()).stopHeating();
    }

    @ParameterizedTest
    @ValueSource(doubles = {21.3, 21.4, 21.5})
    @DisplayName("Stop heating when sensor temperature above programmed temp + upper limit")
    public void testStopHeating_SensorTemperatureAboveUpperLimit(Double sensorTemperature) {
        log.info("Sensor temperature: {}", sensorTemperature);
        ActivationRange activationRange = ActivationRange.builder()
                .upperDifference(0.3d)
                .lowerDifference(0.3d)
                .build();

        Program activeProgram = Program.builder()
                .temperature(PROGRAMMED_TEMPERATURE)
                .build();

        ProgramSchedule programSchedule = ProgramSchedule.builder()
                .activationRange(activationRange)
                .heatingEnabled(true)
                .build();

        Mockito.when(systemService.getSystemStatus()).thenReturn(SystemStatus.builder().build());
        Mockito.when(programService.getActiveProgram()).thenReturn(activeProgram);
        Mockito.when(programService.getProgramSchedule()).thenReturn(programSchedule);
        Mockito.when(temperatureService.getTemperature()).thenReturn(sensorTemperature);

        thermostatService.heatingProcess();
        Mockito.verify(piService, Mockito.never()).startHeating();
        Mockito.verify(piService, Mockito.times(1)).stopHeating();
    }

    @ParameterizedTest
    @ValueSource(doubles = {21.3, 21.4, 21.5})
    @DisplayName("Stop heating when sensor temperature below programmed temp - lower limit")
    public void testStopHeating_SensorTemperatureEqualUpperLimit(Double sensorTemperature) {
        log.info("Sensor temperature: {}", sensorTemperature);
        ActivationRange activationRange = ActivationRange.builder()
                .upperDifference(0.3d)
                .lowerDifference(0.3d)
                .build();

        Program activeProgram = Program.builder()
                .temperature(PROGRAMMED_TEMPERATURE)
                .build();

        ProgramSchedule programSchedule = ProgramSchedule.builder()
                .activationRange(activationRange)
                .heatingEnabled(true)
                .build();

        Mockito.when(systemService.getSystemStatus()).thenReturn(SystemStatus.builder().build());
        Mockito.when(programService.getActiveProgram()).thenReturn(activeProgram);
        Mockito.when(programService.getProgramSchedule()).thenReturn(programSchedule);
        Mockito.when(temperatureService.getTemperature()).thenReturn(sensorTemperature);

        thermostatService.heatingProcess();
        Mockito.verify(piService, Mockito.times(1)).stopHeating();
        Mockito.verify(piService, Mockito.never()).startHeating();
    }

    @ParameterizedTest
    @ValueSource(doubles = {20.8, 21, 21.2})
    @DisplayName("Do nothing when temperature is between upper and lower limit")
    public void testDoNothing_SensorTemperatureBetweenUpperAndLowerLimit(Double sensorTemperature) {
        log.info("Sensor temperature: {}", sensorTemperature);
        ActivationRange activationRange = ActivationRange.builder()
                .upperDifference(0.3d)
                .lowerDifference(0.3d)
                .build();

        Program activeProgram = Program.builder()
                .temperature(PROGRAMMED_TEMPERATURE)
                .build();

        ProgramSchedule programSchedule = ProgramSchedule.builder()
                .activationRange(activationRange)
                .heatingEnabled(true)
                .build();

        Mockito.when(systemService.getSystemStatus()).thenReturn(SystemStatus.builder().build());
        Mockito.when(programService.getActiveProgram()).thenReturn(activeProgram);
        Mockito.when(programService.getProgramSchedule()).thenReturn(programSchedule);
        Mockito.when(temperatureService.getTemperature()).thenReturn(sensorTemperature);

        thermostatService.heatingProcess();
        Mockito.verify(piService, Mockito.never()).startHeating();
        Mockito.verify(piService, Mockito.never()).stopHeating();
    }

}
