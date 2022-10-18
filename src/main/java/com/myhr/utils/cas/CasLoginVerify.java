
package com.myhr.utils.cas;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
import java.util.Map;


@Component
@Slf4j
public class CasLoginVerify {

    private static final String HTTPS = "https";

    @Value("#{${cas.urls}}")
    private Map<String, String> urlMap;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 登陆结果
     */
    public LoginResult login(String username, String password) {
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            String organizationCode = entry.getKey();
            String url = entry.getValue();
            LoginResult result = doLogin(url, username, password);
            if (result.isSuccess()) {
                result.setOrganizationCode(organizationCode);
                return result;
            }
        }
        return null;
    }

    /**
     * 用户登录
     *
     * @param organizationCode 组织编码
     * @param username         用户名
     * @param password         密码
     * @return ticket
     */
    public LoginResult login(String organizationCode, String username, String password) {
        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
            return null;
        }

        LoginResult result = doLogin(url, username, password);
        result.setOrganizationCode(organizationCode);
        return result;
    }

    private LoginResult doLogin(String url, String username, String password) {
        LoginResult result = new LoginResult();
        try {
            url += "/cas/v1/tickets";
            URL u = new URL(url);
            if (HTTPS.equalsIgnoreCase(u.getProtocol())) {
                ignoreSsl();
            }
            HttpURLConnection connection = HttpUtil.openPostConnection(u);
            final String payload = HttpUtil.encodeQueryParam("username", username)
                    + "&" + HttpUtil.encodeQueryParam("password", password);
            final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8));
            out.write(payload);
            out.close();
            final String locationHeader = connection.getHeaderField("location");
            final int responseCode = connection.getResponseCode();
            boolean loginSuccess = (locationHeader != null && responseCode == HttpStatus.CREATED.value());
            result.setSuccess(loginSuccess);
//            logger.event("login")
//                    .msg("doLogin")
//                    .field("url", url)
//                    .field("responseCode", responseCode)
//                    .field("locationHeader", locationHeader)
//                    .info();
            if (loginSuccess) {
                String ticket = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                result.setTicket(ticket);

                log.info("用户登录成功:username:{},ticket:{}",username,ticket);
            }
        } catch (IOException e) {
            result.setSuccess(false);
        }
        return result;
    }

    /**
     * 用户登出
     *
     * @param organizationCode 组织编码
     * @param ticket           ticket
     */
    public void logout(String organizationCode, String ticket) {
        if (StringUtils.isBlank(organizationCode)) {
//            logger.event("verify")
//                    .msg("用户验证失败，组织编码为空")
//                    .field("ticket", ticket)
//                    .error();
            return;
        }

        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
//            logger.event("logout")
//                    .msg("用户登出失败，组织不存在")
//                    .field("organizationCode", organizationCode)
//                    .field("ticket", ticket)
//                    .error();

            return;
        }

        HttpURLConnection connection;
        try {
            url += "/cas/v1/tickets";
            URL u = new URL(url + "/" + ticket);
            if (HTTPS.equalsIgnoreCase(u.getProtocol())) {
                ignoreSsl();
            }
            connection = HttpUtil.openDeleteConnection(u);
            connection.setRequestProperty("Content-type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (HttpStatus.OK.value() == responseCode) {
                // 200：调用成功

            } else if (HttpStatus.CREATED.value() == responseCode) {
                // 201：ticket已经失效
            } else {

            }
        } catch (IOException e) {
        }
    }

    /**
     * 验证ticket是否有效
     *
     * @param organizationCode 组织编码
     * @param ticket           ticket
     */
    public void verify(String organizationCode, String ticket) {
        if (StringUtils.isBlank(organizationCode)) {
//            logger.event("verify")
//                    .msg("用户验证失败，组织编码为空")
//                    .field("ticket", ticket)
//                    .error();
            return;
        }

        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
//            logger.event("verify")
//                    .msg("用户验证失败，组织不存在")
//                    .field("organizationCode", organizationCode)
//                    .field("ticket", ticket)
//                    .error();

            return;
        }

        try {
            url += "/cas/v1/tickets";
            URL u = new URL(url + "/" + ticket);
            if (HTTPS.equalsIgnoreCase(u.getProtocol())) {
                ignoreSsl();
            }
            HttpURLConnection connection = HttpUtil.openGetConnection(u);
            connection.setRequestProperty("Content-type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
            if (connection.getResponseCode() == HttpStatus.NOT_FOUND.value()) {
//                throw new UnauthorizedException(ErrorCode.TICKET_INVALIDATE);
            }
        } catch (IOException e) {
//            throw new SystemException(ErrorCode.SYSTEM_ERROR, e);
        }
    }

    /**
     * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
     */
    @SneakyThrows
    public static void ignoreSsl() {
        HostnameVerifier hv = (urlHostName, session) -> true;
        trustAllHttpsCertificates();
        HttpsURLConnection.setDefaultHostnameVerifier(hv);
    }

    private static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[1];
        TrustManager tm = new CertIgnoreTrustManger();
        trustAllCerts[0] = tm;
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    static class CertIgnoreTrustManger implements TrustManager, X509TrustManager {

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
    }


}
