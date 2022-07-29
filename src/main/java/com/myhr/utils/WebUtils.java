package com.myhr.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author yyf
 * @Date 2022-07-06 14:42
 */
public class WebUtils {
    /**
     * 获取请求
     */
    public static HttpServletRequest getRequest() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        //获取一个请求
        return requestAttributes.getRequest();
    }

    /**
     * 获取会话
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 判断是否为AJAX请求
     *
     * @param request
     * @return
     */
    public static final boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }
}
