package com.sh.thermostat.actuator.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sh.thermostat.actuator.domain.thermostat.ActivationRange;
import com.sh.thermostat.actuator.domain.thermostat.DaySchedule;
import com.sh.thermostat.actuator.domain.thermostat.Program;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import com.sh.thermostat.actuator.service.JsonMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@Component
public class ProgramRepository extends AbstractFileRepository<ProgramSchedule> {

    public ProgramRepository(@Value("${program.repository.file.path:}") String filepath) {
        super(filepath, "/thermostat-repo.json");
    }

    public ProgramSchedule getProgramSchedule() {
        try {
            return JsonMapper.getObjectMapper().readValue(this.readFile(), ProgramSchedule.class);
        } catch (JsonProcessingException e) {
            log.error("Cannot read file from disk, returning default value", e);
            return getDefaultValue();
        }
    }

    public void setProgramSchedule(ProgramSchedule programSchedule) {
        try {
            writeFile(programSchedule.toString());
        } catch (Exception e) {
            log.error("Cannot write program schedule to file", e);
        }
    }

    @SneakyThrows
    @Override
    protected ProgramSchedule getDefaultValue() {
        return ProgramSchedule.builder()
                .heatingEnabled(false)
                .manualMode(false)
                .manualModeTemperature(9.1d)
                .antiFreezeTemperature(5.0d)
                .activationRange(ActivationRange.builder()
                        .lowerDifference(0.25d)
                        .upperDifference(0.3d)
                        .build())
                .normalDaySchedules(Stream.of(
                        DaySchedule.builder()
                                .day(DayOfWeek.MONDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.TUESDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.WEDNESDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.THURSDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.FRIDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.SATURDAY)
                                .programs(getDefaultPrograms())
                                .build(),
                        DaySchedule.builder()
                                .day(DayOfWeek.SUNDAY)
                                .programs(getDefaultPrograms())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }

    private List<Program> getDefaultPrograms() {
        return Stream.of(Program.builder()
                        .start(LocalTime.MIN)
                        .end(LocalTime.of(7, 49, 59))
                        .temperature(19d)
                        .build(),
                Program.builder()
                        .start(LocalTime.of(7, 50))
                        .end(LocalTime.of(22, 29, 59))
                        .temperature(21d)
                        .build(),
                Program.builder()
                        .start(LocalTime.of(22, 30))
                        .end(LocalTime.MAX)
                        .temperature(19d)
                        .build())
                .collect(Collectors.toList());
    }
}
