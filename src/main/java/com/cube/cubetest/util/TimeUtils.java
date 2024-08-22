package com.cube.cubetest.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static LocalDateTime parseUpdateTime(String updatedISO) {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(updatedISO);
        return zonedDateTime.toLocalDateTime();
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}