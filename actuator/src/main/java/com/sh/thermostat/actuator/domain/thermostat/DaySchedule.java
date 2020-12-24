package com.sh.thermostat.actuator.domain.thermostat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DaySchedule {

    @JsonProperty("day")
    private DayOfWeek day;

    @JsonProperty("programs")
    private List<Program> programs;

}
