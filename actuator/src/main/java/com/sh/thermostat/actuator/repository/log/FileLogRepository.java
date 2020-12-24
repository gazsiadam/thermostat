package com.sh.thermostat.actuator.repository.log;

import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import com.sh.thermostat.actuator.repository.AbstractFileRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Log4j2
@Component
@ConditionalOnExpression("${aws.dynamodb.enabled:false} == false")
public class FileLogRepository extends AbstractFileRepository<ThermostatLog> implements LogRepository {

    public FileLogRepository(@Value("${log.repository.file.path:}") String filepath) {
        super(filepath, "/log-repo.json");
    }

    public void persistLog(ThermostatLog thermostatLog) {
        try {
            appendFile(thermostatLog.toString());
        } catch (IOException e) {
            log.error("Cannot write log to file!", e);
        }
    }

    @Override
    protected ThermostatLog getDefaultValue() {
        return ThermostatLog.builder().build();
    }
}
