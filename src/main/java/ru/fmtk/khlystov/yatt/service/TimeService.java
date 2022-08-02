package ru.fmtk.khlystov.yatt.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

@Service
public class TimeService {

    public LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

    public LocalDate getDate() {
        return getDateTime().toLocalDate();
    }
}
