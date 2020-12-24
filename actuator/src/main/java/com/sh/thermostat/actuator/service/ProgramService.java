package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.log.LogType;
import com.sh.thermostat.actuator.domain.log.ThermostatLog;
import com.sh.thermostat.actuator.domain.thermostat.DaySchedule;
import com.sh.thermostat.actuator.domain.thermostat.ExceptionalDaySchedule;
import com.sh.thermostat.actuator.domain.thermostat.Program;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import com.sh.thermostat.actuator.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProgramService {

    private final DateTimeService dateTimeService;
    private final ProgramRepository programRepository;
    private final LogService logService;

    public Program getActiveProgram() {
        LocalDate today = this.dateTimeService.nowLocalDate();
        DayOfWeek dayName = DayOfWeek.from(today);

        ProgramSchedule programSchedule = programRepository.getProgramSchedule();

        // Find Exceptional program
        Optional<ExceptionalDaySchedule> exceptionalDaySchedule = programSchedule.getExceptionalDaySchedules().stream()
                .filter(ds -> today.equals(ds.getDate()))
                .findFirst();

        if (exceptionalDaySchedule.isPresent()) {
            Optional<Program> activeProgram = findActiveProgram(exceptionalDaySchedule.get().getPrograms());
            if (activeProgram.isPresent()) {
                return activeProgram.get();
            }
        }

        // Find normal day schedule
        Optional<DaySchedule> normalDaySchedule = programSchedule.getNormalDaySchedules().stream()
                .filter(ds -> dayName.equals(ds.getDay()))
                .findFirst();

        if (normalDaySchedule.isPresent()) {
            Optional<Program> activeProgram = findActiveProgram(normalDaySchedule.get().getPrograms());
            if (activeProgram.isPresent()) {
                return activeProgram.get();
            }
        }

        log.warn("Active program cannot be found. Default program is used!");
        return getDefaultProgram();
    }

    private Optional<Program> findActiveProgram(List<Program> programs) {
        LocalTime now = this.dateTimeService.nowLocalTime();
        return programs.stream()
                .filter(p -> now.isAfter(p.getStart()) || p.getStart().equals(now))
                .filter(p -> p.getEnd().truncatedTo(ChronoUnit.MINUTES).equals(now.truncatedTo(ChronoUnit.MINUTES))
                        || now.isBefore(p.getEnd().truncatedTo(ChronoUnit.MINUTES)))
                .findAny();
    }

    public Program getDefaultProgram() {
        return Program.builder()
                .temperature(5d)
                .build();
    }

    public ProgramSchedule getProgramSchedule() {
        return this.programRepository.getProgramSchedule();
    }

    public void setProgramSchedule(ProgramSchedule programSchedule) {
        this.programRepository.setProgramSchedule(programSchedule);
    }

    public void setManualTemperature(Double manualTemperature) {
        ProgramSchedule programSchedule = this.getProgramSchedule();
        programSchedule.setManualMode(true);
        programSchedule.setManualModeTemperature(manualTemperature);
        this.programRepository.setProgramSchedule(programSchedule);
        this.logService.log(ThermostatLog.builder()
                .date(dateTimeService.nowLocalDateTime())
                .logType(LogType.SYSTEM_EVENT)
                .data("Set manual mode to, temperature = " + manualTemperature)
                .build());
    }

    public void clearManualMode() {
        ProgramSchedule programSchedule = this.getProgramSchedule();
        programSchedule.setManualMode(false);
        this.programRepository.setProgramSchedule(programSchedule);
        this.logService.log(ThermostatLog.builder()
                .date(dateTimeService.nowLocalDateTime())
                .logType(LogType.SYSTEM_EVENT)
                .data("Clear manual mode")
                .build());
    }
}
