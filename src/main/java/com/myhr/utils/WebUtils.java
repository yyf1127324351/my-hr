package com.myhr.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

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

    public static void redirectUrl(HttpServletResponse response, String redirectUrl, boolean browserRefresh) throws IOException {
        if(browserRefresh) {
            response.setContentType(ContentType.create("text/html", "UTF-8").toString());
            String content = FreeMarkerUtil.process("<!DOCTYPE html><html><script type='text/javascript'>!function() { var redirectUrl='${redirectUrl}'; if(window.opener) { window.opener.location.href=redirectUrl; window.close(); }else { window.top.window.location.href=redirectUrl; }}()</script></html>",
                    Collections.singletonMap("redirectUrl", redirectUrl));
            IOUtils.write(content, response.getOutputStream());
        }else {
            response.sendRedirect(redirectUrl);
        }
    }
}
