package com.sh.thermostat.actuator.domain.log;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sh.thermostat.actuator.domain.JsonDomainObject;
import com.sh.thermostat.actuator.repository.dynamodb.DynamoDBTypeConvertedLocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@DynamoDBTable(tableName = "thermostat_log")
@EqualsAndHashCode(callSuper = true)
public class ThermostatLog extends JsonDomainObject {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @DynamoDBTypeConvertedLocalDateTime
    @DynamoDBHashKey(attributeName = "date")
    private LocalDateTime date;

    @JsonProperty
    @DynamoDBHashKey(attributeName = "log_type")
    @DynamoDBTypeConvertedEnum
    private LogType logType;

    @JsonProperty
    @DynamoDBTypeConvertedJson
    private String data;

}
