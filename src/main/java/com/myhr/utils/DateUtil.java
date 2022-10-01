package com.myhr.utils;

import java.time.LocalDate;
import java.util.Date;

/**
 * @Description
 * @Author yyf
 * @Date 2022-09-30 23:12
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
