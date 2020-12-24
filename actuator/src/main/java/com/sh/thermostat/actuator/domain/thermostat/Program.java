package com.sh.thermostat.actuator.domain.thermostat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Program {

    @JsonProperty
    private LocalTime start;

    @JsonProperty
    private LocalTime end;

    @JsonProperty
    private Double temperature;

}
