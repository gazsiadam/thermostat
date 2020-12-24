package com.sh.thermostat.actuator.domain;

import com.sh.thermostat.actuator.service.JsonMapper;
import lombok.SneakyThrows;

public abstract class JsonDomainObject {

    @SneakyThrows
    @Override
    public final String toString() {
        return JsonMapper.getObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this);
    }

}
