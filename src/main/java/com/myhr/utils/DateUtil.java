package com.myhr.utils;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Description
 * @Author yyf
 */
public class DateUtil {


    /**
     * @Description  yyyy-MM-dd 获取当天日期
     */
    public static String getTodayDate() {
        LocalDate dateD = LocalDate.now();
        String dateStr = dateD.toString();
        return dateStr;
    }

    public static Date toDateTime(String dateTime, String pattern) {
        DateTimeFormatter pattern1 = DateTimeFormatter.ofPattern(pattern);
        LocalDateTime dateTime1 = LocalDateTime.parse(dateTime, pattern1);
        Date date = Date.from( dateTime1.atZone( ZoneId.systemDefault()).toInstant());
        return date;
    }


    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }


    /**
     * @Description endDate >= startDate ?
     */
    public static boolean isAfter(String endDate, String startDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return end.isAfter(start);
    }

    /**
     * @Description endDate <= startDate ?
     */
    public static boolean isBefore(String endDate, String startDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return end.isBefore(start);
    }

    /**
     * @Description endDate == startDate ?
     */
    public static boolean isEqual(String endDate, String startDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return end.isEqual(start);
    }

    /**
     * @Description  yyyy-MM-dd 减天数
     */
    public static String subDayDate(String date, Integer subDays) {
        LocalDate dateD = LocalDate.parse(date);
        dateD = dateD.minusDays(subDays);
        String dateStr = dateD.toString();
        return dateStr;
    }

}
