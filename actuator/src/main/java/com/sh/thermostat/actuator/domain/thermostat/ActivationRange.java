package com.sh.thermostat.actuator.domain.thermostat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationRange {

    private double upperDifference;
    private double lowerDifference;

}
