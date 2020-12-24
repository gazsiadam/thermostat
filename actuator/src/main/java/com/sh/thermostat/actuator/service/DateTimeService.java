package com.sh.thermostat.actuator.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class DateTimeService {

    public LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now();
    }

    public LocalDate nowLocalDate() {
        return LocalDate.now();
    }

    public LocalTime nowLocalTime() {
        return LocalTime.now();
    }

}
