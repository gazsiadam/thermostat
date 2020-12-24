package com.sh.thermostat.actuator.domain.thermostat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ExceptionalDaySchedule extends DaySchedule {

    private LocalDate date;

    ExceptionalDaySchedule(DayOfWeek day, List<Program> programs) {
        super(day, programs);
    }
}
