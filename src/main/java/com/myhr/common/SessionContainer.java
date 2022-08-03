package com.myhr.common;


import com.myhr.hr.vo.UserVo;

/**
 * @Description 用户session容器
 * @Author yyf
 * @Date 2022-07-06 15:35
 */
public class SessionContainer {

    private static final ThreadLocal<UserVo> sessionThreadLocal = new ThreadLocal<>();

    public static UserVo getSession() {
        return sessionThreadLocal.get();
    }

    public static void setSession(UserVo c) { sessionThreadLocal.set(c); }

    public static void clear() {
        sessionThreadLocal.set(null);
    }


    public static String getUserName() {
        return getSession() != null ? getSession().getUserName() : null;
    }

    public static String getLoginName() {
        return getSession() != null ? getSession().getLoginName() : null;
    }

    public static Long getUserId() {
        return getSession() != null ? getSession().getId() : null;
    }

}
