package com.sh.thermostat.actuator.domain.thermostat;

import com.sh.thermostat.actuator.domain.system.SystemHealth;
import com.sh.thermostat.actuator.domain.system.SystemStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ThermostatStatus {

    private Double lastMeasuredTemperature;
    private Double lastMeasuredOutsideTemperature;
    private ProgramSchedule programSchedule;
    private Program activeProgram;
    private Boolean isHeatingOn;
    private SystemStatus systemStatus;
    private LocalDateTime time;

}
