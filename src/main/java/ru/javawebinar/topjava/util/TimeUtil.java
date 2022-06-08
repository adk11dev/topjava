package ru.javawebinar.topjava.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtil {
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    private TimeUtil() {
    }

    public static boolean isBetweenHalfOpen(LocalTime lt, LocalTime startTime, LocalTime endTime) {
        return lt.compareTo(startTime) >= 0 && lt.compareTo(endTime) < 0;
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(dateTimeFormatter);
    }
}