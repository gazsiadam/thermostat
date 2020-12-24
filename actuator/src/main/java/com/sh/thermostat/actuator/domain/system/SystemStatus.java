package com.sh.thermostat.actuator.domain.system;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SystemStatus {

    private SystemHealth systemHealth;
    private boolean temperatureSensorMalfunction;

}
