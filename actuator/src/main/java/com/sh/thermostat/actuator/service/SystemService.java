package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.system.SystemHealth;
import com.sh.thermostat.actuator.domain.system.SystemStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SystemService {

    @Getter
    private final SystemStatus systemStatus = SystemStatus.builder().build();

    public void setTemperatureSensorMalfunction() {
        systemStatus.setTemperatureSensorMalfunction(true);
        systemStatus.setSystemHealth(SystemHealth.ERROR);
    }
}
