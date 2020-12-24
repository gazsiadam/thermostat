package com.sh.thermostat.actuator.domain.log;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum LogDataType {

    DOUBLE(Double.class),
    STRING(String.class);

    private final Class<?> dataType;

}
