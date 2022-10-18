
package com.myhr.utils.cas;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



/**
 *
 * 通过 REST协议 验证cas登录
 * */

@Component
@Slf4j
public class CasLoginVerify {

    private static final String HTTPS = "https";

    @Value("#{${cas.urls}}")
    private Map<String, String> urlMap;

    @Value("${cas.client.index}")
    private String casClientIndex;


    /**
     * cas验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录结果
     */
    public LoginResult doCasLoginVerify(String username, String password) {
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            String organizationCode = entry.getKey();
            String url = entry.getValue();
            LoginResult loginResult = doVerify(organizationCode,url, username, password);
            if (null != loginResult && loginResult.isSuccess()) {
                return loginResult;
            }
        }
        return null;
    }

    /**
     * cas验证
     *
     * @param organizationCode 组织编码
     * @param username         用户名
     * @param password         密码
     * @return 登录结果
     */
    public LoginResult doCasLoginVerify(String organizationCode, String username, String password) {
        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
            return null;
        }
        return this.doVerify(organizationCode, url, username, password);
    }

    /**
     * cas验证
     *
     *
     * @param organizationCode 组织编码
     * @param casUrl cas服务地址
     * @param username  用户名
     * @param password  密码
     * @return 登录结果
     */
    private LoginResult doVerify(String organizationCode, String casUrl, String username, String password) {
        //获取TGT
        String TGT = this.getCasTGT(casUrl, username, password);
        if (StringUtils.isNotBlank(TGT)) {
            LoginResult loginResult = new LoginResult();
            loginResult.setTgt(TGT);
            //获取ST
            String ST = this.getCasST(casUrl,TGT);
            if (StringUtils.isNotBlank(ST)) {
                loginResult.setSt(ST);
                //TODO 进行验证
                String st = ST;
                String indexUrl = casClientIndex + "?ticket=" + ST;
                String validateUrl = casUrl + "/p3/serviceValidate" + "?service=" + casClientIndex + "&ticket=" + ST;

                System.out.println(st);
                System.out.println(indexUrl);
                System.out.println(validateUrl);

                //返回登录结果
                loginResult.setOrganizationCode(organizationCode);
                loginResult.setSuccess(true);
                return loginResult;
            }

        }
        return null;
    }

    /**
     * @Description 获取TGT
     * @param casUrl cas服务地址
     * @param username 用户名
     * @param password  密码
     * @return TGT
     */
    private String getCasTGT(String casUrl, String username, String password) {
        String ticket = "";
        try {
            casUrl += "/cas/v1/tickets";
            URL u = new URL(casUrl);
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
            if (loginSuccess) {
                ticket = locationHeader.substring(locationHeader.lastIndexOf("/") + 1);
                log.info("获取TGT成功:username:{},TGT:{}",username,ticket);
                return ticket;
            }
        } catch (IOException e) {
            log.error("获取TGT异常:" + e.getMessage(), e);
            log.error("获取TGT异常:username{},调用服务{}", username, casUrl);
            return ticket;
        }
        return ticket;
    }

    /**
     * @Description 获取ST
     * @param casUrl cas服务地址
     * @param tgt TGT
     * @return ST
     */
    private String getCasST(String casUrl, String tgt) {
        String ST = "";
        casUrl = casUrl + "/cas/v1/tickets/" + tgt;
        HttpPost post = new HttpPost(casUrl);
        CloseableHttpClient client = HttpClientBuilder.create().build();
        List<BasicNameValuePair> parameters = new ArrayList<>();
        parameters.add(new BasicNameValuePair("service", casClientIndex));
        try {
            HttpEntity entity = new UrlEncodedFormEntity(parameters, "utf-8");
            post.setEntity(entity);
            HttpResponse response = client.execute(post);
            HttpEntity entity2 = response.getEntity();
            ST = EntityUtils.toString(entity2);
            log.error("获取ST成功:ST:{},调用服务:{}", ST, casUrl);
        } catch (Exception e) {
            log.error("获取ST异常:" + e.getMessage(), e);
            log.error("获取ST异常:TGT:{},调用服务:{}", tgt, casUrl);
            return ST;
        }
        return ST;
    }






    /**
     * 用户登出
     *
     * @param organizationCode 组织编码
     * @param ticket           ticket
     */
    public void logout(String organizationCode, String ticket) {
        if (StringUtils.isBlank(organizationCode)) {
            return;
        }

        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
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
            } else if (HttpStatus.NOT_FOUND.value() == responseCode) {
//                throw new UnauthorizedException(ErrorCode.TICKET_INVALIDATE);
            }else {

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
            return;
        }

        String url = urlMap.get(organizationCode);
        if (StringUtils.isBlank(url)) {
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
