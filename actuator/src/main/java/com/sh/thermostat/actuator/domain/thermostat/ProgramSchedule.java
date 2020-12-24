package com.sh.thermostat.actuator.domain.thermostat;

import com.sh.thermostat.actuator.domain.JsonDomainObject;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProgramSchedule extends JsonDomainObject {

    private boolean heatingEnabled;
    private boolean manualMode;
    private double antiFreezeTemperature;
    private double manualModeTemperature;
    private double temperatureSensorOffset;

    @Builder.Default
    private ActivationRange activationRange = new ActivationRange(0.5d, 0.5d);

    @Builder.Default
    private List<DaySchedule> normalDaySchedules = new ArrayList<>();

    @Builder.Default
    private List<ExceptionalDaySchedule> exceptionalDaySchedules = new ArrayList<>();

}
