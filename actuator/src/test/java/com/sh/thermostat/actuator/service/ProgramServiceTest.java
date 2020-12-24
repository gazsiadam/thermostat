package com.sh.thermostat.actuator.service;

import com.sh.thermostat.actuator.domain.thermostat.DaySchedule;
import com.sh.thermostat.actuator.domain.thermostat.Program;
import com.sh.thermostat.actuator.domain.thermostat.ProgramSchedule;
import com.sh.thermostat.actuator.repository.ProgramRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Test cases:
 * 1. now: 10:00, program: 8-10, 10-16, expected active program: 10-16 (start of program)
 * 2. now: 10:30, program: 8-10, 10-16, expected active program: 10-16 (middle of program)
 * 3. now: 9:59:59, program: 8-10, 10-16, expected active program: 8-10 (end of program)
 * 4. now: 0:00. program: 0-8, 8-10, expected active program: 0-8 (start of day)
 * 5. now: 23:59. program: 16-22,22-23:59, 0-8, expected active program: 23-23:59 (end of day)
 */

@ExtendWith(MockitoExtension.class)
public class ProgramServiceTest {

    @InjectMocks
    private ProgramService programService;

    @Mock
    private ProgramRepository programRepository;

    @Mock
    private DateTimeService dateTimeService;

    @DisplayName("Case 1 - Start of program")
    @Test
    public void testGetActiveProgram_Case1_StartOfProgram() {
        LocalDateTime mockedNow = LocalDateTime.of(2020, 10, 10, 10, 0);

        LocalTime startP1 = LocalTime.of(8, 0);
        LocalTime endP1 = LocalTime.of(9, 59);
        Double temp1 = 22d;

        LocalTime startP2 = LocalTime.of(10, 0);
        LocalTime endP2 = LocalTime.of(15, 59);
        Double temp2 = 22d;

        List<Program> programs = Stream.of(
                createProgram(startP1, endP1, temp1),
                createProgram(startP2, endP2, temp2)
        ).collect(Collectors.toList());

        List<DaySchedule> daySchedules = Stream.of(
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow))
                        .programs(programs)
                        .build()
        ).collect(Collectors.toList());

        // Mock and set program
        Mockito.when(programRepository.getProgramSchedule()).thenReturn(getProgramSchedule(daySchedules));
        Mockito.when(dateTimeService.nowLocalDate()).thenReturn(mockedNow.toLocalDate());
        Mockito.when(dateTimeService.nowLocalTime()).thenReturn(mockedNow.toLocalTime());

        Program activeProgram = programService.getActiveProgram();
        Assertions.assertNotNull(activeProgram);
        Assertions.assertEquals(startP2, activeProgram.getStart());
        Assertions.assertEquals(endP2, activeProgram.getEnd());
        Assertions.assertEquals(temp2, activeProgram.getTemperature());
    }

    @DisplayName("Case 2 - Middle of program")
    @Test
    public void testGetActiveProgram_Case2_MiddleOfProgram() {
        LocalDateTime mockedNow = LocalDateTime.of(2020, 10, 10, 10, 30);

        LocalTime startP1 = LocalTime.of(8, 0);
        LocalTime endP1 = LocalTime.of(9, 59);
        Double temp1 = 22d;

        LocalTime startP2 = LocalTime.of(10, 0);
        LocalTime endP2 = LocalTime.of(15, 59);
        Double temp2 = 22d;

        List<Program> programs = Stream.of(
                createProgram(startP1, endP1, temp1),
                createProgram(startP2, endP2, temp2)
        ).collect(Collectors.toList());

        List<DaySchedule> daySchedules = Stream.of(
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow))
                        .programs(programs)
                        .build()
        ).collect(Collectors.toList());

        // Mock and set program
        Mockito.when(programRepository.getProgramSchedule()).thenReturn(getProgramSchedule(daySchedules));
        Mockito.when(dateTimeService.nowLocalDate()).thenReturn(mockedNow.toLocalDate());
        Mockito.when(dateTimeService.nowLocalTime()).thenReturn(mockedNow.toLocalTime());

        Program activeProgram = programService.getActiveProgram();
        Assertions.assertNotNull(activeProgram);
        Assertions.assertEquals(startP2, activeProgram.getStart());
        Assertions.assertEquals(endP2, activeProgram.getEnd());
        Assertions.assertEquals(temp2, activeProgram.getTemperature());
    }

    @DisplayName("Case 3 - Middle of program")
    @Test
    public void testGetActiveProgram_Case3_EndOfProgram() {
        LocalDateTime mockedNow = LocalDateTime.of(2020, 10, 10, 9, 59, 59);

        LocalTime startP1 = LocalTime.of(8, 0);
        LocalTime endP1 = LocalTime.of(9, 59);
        Double temp1 = 22d;

        LocalTime startP2 = LocalTime.of(10, 0);
        LocalTime endP2 = LocalTime.of(15, 59);
        Double temp2 = 22d;

        List<Program> programs = Stream.of(
                createProgram(startP1, endP1, temp1),
                createProgram(startP2, endP2, temp2)
        ).collect(Collectors.toList());

        List<DaySchedule> daySchedules = Stream.of(
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow))
                        .programs(programs)
                        .build()
        ).collect(Collectors.toList());

        // Mock and set program
        Mockito.when(programRepository.getProgramSchedule()).thenReturn(getProgramSchedule(daySchedules));
        Mockito.when(dateTimeService.nowLocalDate()).thenReturn(mockedNow.toLocalDate());
        Mockito.when(dateTimeService.nowLocalTime()).thenReturn(mockedNow.toLocalTime());

        Program activeProgram = programService.getActiveProgram();
        Assertions.assertNotNull(activeProgram);
        Assertions.assertEquals(startP1, activeProgram.getStart());
        Assertions.assertEquals(endP1, activeProgram.getEnd());
        Assertions.assertEquals(temp1, activeProgram.getTemperature());
    }

    @DisplayName("Case 4 - Start of day")
    @Test
    public void testGetActiveProgram_Case4_StartOfDay() {
        LocalDateTime mockedNow = LocalDateTime.of(2020, 10, 10, 0, 0);

        LocalTime startP1 = LocalTime.of(0, 0);
        LocalTime endP1 = LocalTime.of(7, 59);
        Double temp1 = 19d;

        LocalTime startP2 = LocalTime.of(8, 0);
        LocalTime endP2 = LocalTime.of(9, 59);
        Double temp2 = 21d;

        List<Program> programs = Stream.of(
                createProgram(startP1, endP1, temp1),
                createProgram(startP2, endP2, temp2)
        ).collect(Collectors.toList());

        List<DaySchedule> daySchedules = Stream.of(
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow))
                        .programs(programs)
                        .build()
        ).collect(Collectors.toList());

        // Mock and set program
        Mockito.when(programRepository.getProgramSchedule()).thenReturn(getProgramSchedule(daySchedules));
        Mockito.when(dateTimeService.nowLocalDate()).thenReturn(mockedNow.toLocalDate());
        Mockito.when(dateTimeService.nowLocalTime()).thenReturn(mockedNow.toLocalTime());

        Program activeProgram = programService.getActiveProgram();
        Assertions.assertNotNull(activeProgram);
        Assertions.assertEquals(startP1, activeProgram.getStart());
        Assertions.assertEquals(endP1, activeProgram.getEnd());
        Assertions.assertEquals(temp1, activeProgram.getTemperature());
    }

    @DisplayName("Case 5 - End of day")
    @Test
    public void testGetActiveProgram_Case5_EndOfDay() {
        LocalDateTime mockedNow = LocalDateTime.of(2020, 10, 10, 23, 59, 59);

        LocalTime startDay1P1 = LocalTime.of(16, 0);
        LocalTime endDay1P1 = LocalTime.of(21, 59);
        Double tempDay1P1 = 21d;

        LocalTime startDay1P2 = LocalTime.of(22, 0);
        LocalTime endDay1P2 = LocalTime.of(23, 59);
        Double tempDay1P2 = 19d;

        LocalTime startDay2P1 = LocalTime.of(0, 0);
        LocalTime endDay2P1 = LocalTime.of(7, 59);
        Double tempDay2P1 = 19d;

        List<Program> programsDay1 = Stream.of(
                createProgram(startDay1P1, endDay1P1, tempDay1P1),
                createProgram(startDay1P2, endDay1P2, tempDay1P2)
        ).collect(Collectors.toList());

        List<Program> programsDay2 = Stream.of(
                createProgram(startDay2P1, endDay2P1, tempDay2P1)
        ).collect(Collectors.toList());

        List<DaySchedule> daySchedules = Stream.of(
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow))
                        .programs(programsDay1)
                        .build(),
                DaySchedule.builder()
                        .day(DayOfWeek.from(mockedNow).plus(1))
                        .programs(programsDay2)
                        .build()
        ).collect(Collectors.toList());

        // Mock and set program
        Mockito.when(programRepository.getProgramSchedule()).thenReturn(getProgramSchedule(daySchedules));
        Mockito.when(dateTimeService.nowLocalDate()).thenReturn(mockedNow.toLocalDate());
        Mockito.when(dateTimeService.nowLocalTime()).thenReturn(mockedNow.toLocalTime());

        Program activeProgram = programService.getActiveProgram();
        Assertions.assertNotNull(activeProgram);
        Assertions.assertEquals(startDay1P2, activeProgram.getStart());
        Assertions.assertEquals(endDay1P2, activeProgram.getEnd());
        Assertions.assertEquals(tempDay1P2, activeProgram.getTemperature());
    }

    private ProgramSchedule getProgramSchedule(List<DaySchedule> daySchedules) {
        return ProgramSchedule.builder()
                .normalDaySchedules(daySchedules)
                .build();
    }

    private Program createProgram(LocalTime start, LocalTime end, Double temperature) {
        return Program.builder()
                .start(start)
                .end(end)
                .temperature(temperature)
                .build();
    }

}
