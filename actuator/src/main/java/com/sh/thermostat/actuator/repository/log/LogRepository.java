package com.sh.thermostat.actuator.repository.log;

import com.sh.thermostat.actuator.domain.log.ThermostatLog;

public interface LogRepository {

    void persistLog(ThermostatLog thermostatLog);

}
