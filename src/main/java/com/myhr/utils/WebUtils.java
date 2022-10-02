package com.myhr.utils;

import org.apache.commons.io.IOUtils;
import org.apache.http.entity.ContentType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
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
            String content = FreeMarkerUtils.process("<!DOCTYPE html><html><script type='text/javascript'>!function() { var redirectUrl='${redirectUrl}'; if(window.opener) { window.opener.location.href=redirectUrl; window.close(); }else { window.top.window.location.href=redirectUrl; }}()</script></html>",
                    Collections.singletonMap("redirectUrl", redirectUrl));
            IOUtils.write(content, response.getOutputStream());
        }else {
            response.sendRedirect(redirectUrl);
        }
    }


    public static void downloadExcelHeader(HttpServletRequest request,
                                           HttpServletResponse response,
                                           String fileName) throws IOException {
        String name = fileName.toLowerCase();
        if(name.endsWith(".xlsx") || name.endsWith(".xlsm")) {
            downloadHeader(request, response, fileName, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }else if(name.endsWith(".xls") || name.endsWith(".xlm")) {
            downloadHeader(request, response, fileName, "application/vnd.ms-excel;charset=utf-8");
        }else {
            throw new IOException("不支持的文件名称后缀");
        }
    }

    public static void downloadHeader(HttpServletRequest request,
                                      HttpServletResponse response,
                                      String fileName,
                                      String contentType) throws IOException {
        response.setContentType(contentType);
        String header = request.getHeader("User-Agent").toUpperCase();
        if (header.contains("MSIE") || header.contains("TRIDENT") || header.contains("EDGE")) {
            fileName = URLEncoder.encode(fileName, "utf-8");
            fileName = fileName.replace("+", "%20");    //IE下载文件名空格变+号问题
        } else {
            fileName = new String(fileName.getBytes(), "ISO8859-1");
        }
        Cookie fileDownloadCoolie = new Cookie("fileDownload", "true");
        fileDownloadCoolie.setPath("/");
        response.addCookie(fileDownloadCoolie);
        response.setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
    }
}
