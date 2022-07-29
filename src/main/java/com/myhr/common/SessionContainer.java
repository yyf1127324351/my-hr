package com.myhr.common;


import com.myhr.hr.OperatorDto;

/**
 * @Description 用户session容器
 * @Author yyf
 * @Date 2022-07-06 15:35
 */
public class SessionContainer {

    private static final ThreadLocal<OperatorDto> sessionThreadLocal = new ThreadLocal<>();

    public static OperatorDto getSession() {
        return sessionThreadLocal.get();
    }

    public static void setSession(OperatorDto c) { sessionThreadLocal.set(c); }

    public static void clear() {
        sessionThreadLocal.set(null);
    }


    public static String getOperatorName() {
        return getSession() != null ? getSession().getOperatorName() : null;
    }

    public static String getOperatorLoginName() {
        return getSession() != null ? getSession().getOperatorLoginName() : null;
    }

    public static Long getOperatorId() {
        return getSession() != null ? getSession().getOperatorId() : null;
    }

}
