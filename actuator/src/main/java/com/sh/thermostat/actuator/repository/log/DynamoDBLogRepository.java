package com.sh.thermostat.actuator.repository.log;

import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import lombok.RequiredArgsConstructor;
import org.socialsignin.spring.data.dynamodb.core.DynamoDBTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${aws.dynamodb.enabled:false} == true")
public class DynamoDBLogRepository implements LogRepository {

    private final DynamoDBTemplate dynamoDBTemplate;

    @Override
    public void persistLog(ThermostatLog thermostatLog) {
        dynamoDBTemplate.save(thermostatLog);
    }

}
