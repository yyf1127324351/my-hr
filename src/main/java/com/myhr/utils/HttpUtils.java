package com.myhr.utils;


import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.*;

public class HttpUtils {

    /**
     * get
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @return
     * @throws IOException
     */
    public static HttpResponse doGet(String host, String path,
                                     Map<String, String> headers,
                                     Map<String, String> querys)
            throws IOException {
        HttpClient httpClient = wrapClient(host);

        HttpGet request = new HttpGet(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doGet(String url)
            throws IOException {
        return doGet(url, Collections.emptyMap());
    }

    public static HttpResponse doGet(String url, Map<String, String> headers)
            throws IOException {
        HttpClient httpClient = wrapClient(url);
        HttpGet request = new HttpGet(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }
        return httpClient.execute(request);
    }

    /**
     * post form
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @param bodys
     * @return
     * @throws IOException
     */
    public static HttpResponse doPost(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      Map<String, String> bodys)
            throws IOException {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (bodys != null) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

            for (String key : bodys.keySet()) {
                nameValuePairList.add(new BasicNameValuePair(key, bodys.get(key)));
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return httpClient.execute(request);
    }

    public static <T> T convertResponse(HttpResponse response, Class<T> clazz) throws IOException {
        if(response.getStatusLine().getStatusCode() == 200) {
            return JsonUtils.toObject(response.getEntity().getContent(), clazz);
        }
        return null;
    }

    /**
     * Post String
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @param body
     * @return
     * @throws IOException
     */
    public static HttpResponse doPost(String host, String path,
                                      Map<String, String> headers,
                                      Map<String, String> querys,
                                      String body)
            throws IOException {
        HttpClient httpClient = wrapClient(host);

        HttpPost request = new HttpPost(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    public static HttpResponse doPostForm(String url, Map<String, Object> form) throws IOException {
        return doPostForm(url, Collections.emptyMap(), form);
    }

    public static HttpResponse doPostForm(String url, Map<String, String> headers, Map<String, Object> form) throws IOException {
        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (MapUtils.isNotEmpty(form)) {
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            for (String key : form.keySet()) {
                Object value = form.get(key);
                if(value != null) {
                    nameValuePairList.add(new BasicNameValuePair(key, form.get(key).toString()));
                }
            }
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairList, "utf-8");
            formEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
            request.setEntity(formEntity);
        }

        return wrapClient(url).execute(request);
    }

    /**
     * 发送POST情趣
     * @param url 请求体制
     * @param headers 请求头
     * @param form form表单参数  可包含普通参数和文件
     * @return
     * @throws IOException
     */
    public static HttpResponse doPostFormMultiPart(String url, Map<String, String> headers, Map<String, Object> form) throws IOException {
        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (MapUtils.isNotEmpty(form)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            // 设置模式 防止乱码
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.setCharset(StandardCharsets.UTF_8);
            final ContentType TEXT_CONTENT_TYPE = ContentType.create("text/plain", StandardCharsets.UTF_8);
            for (Map.Entry<String, Object> e : form.entrySet()) {
                if (Objects.isNull(e.getKey())||Objects.isNull(e.getValue())){
                    continue;
                }
                if (e.getValue() instanceof String){
                    builder.addPart(e.getKey(), new StringBody(e.getValue().toString(),TEXT_CONTENT_TYPE));
                }else if(e.getValue() instanceof List){
                    List<File> files = (List<File>) e.getValue();
                    // 把文件加到HTTP的post请求中
                    for (File file : files) {
                        builder.addBinaryBody(
                                e.getKey(),
                                new FileInputStream(file),
                                ContentType.APPLICATION_OCTET_STREAM,
                                file.getName()
                        );
                    }
                }else if (e.getValue() instanceof File) {
                    File file = (File) e.getValue();
                    // 把文件加到HTTP的post请求中
                    builder.addBinaryBody(
                            e.getKey(),
                            new FileInputStream(file),
                            ContentType.APPLICATION_OCTET_STREAM,
                            file.getName()
                    );
                }else {
                    continue;
                }
            }
            HttpEntity entity = builder.setCharset(StandardCharsets.UTF_8).build();
            request.setEntity(entity);
        }

        return wrapClient(url).execute(request);
    }

    public static HttpResponse doPost(String url, String body) throws IOException {
        return doPost(url, Collections.singletonMap("Content-Type", ContentType.APPLICATION_JSON.toString()), body);
    }

    public static HttpResponse doPost(String url, Map<String, String> headers, String body)
            throws IOException {
        HttpClient httpClient = wrapClient(url);

        HttpPost request = new HttpPost(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Put String
     * @param url
     * @param headers
     * @param body
     * @return
     * @throws IOException
     */
    public static HttpResponse doPut(String url, Map<String, String> headers, String body)
            throws IOException {
        HttpClient httpClient = wrapClient(url);

        HttpPut request = new HttpPut(url);
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        if (StringUtils.isNotBlank(body)) {
            request.setEntity(new StringEntity(body, "utf-8"));
        }

        return httpClient.execute(request);
    }

    /**
     * Delete
     *
     * @param host
     * @param path
     * @param headers
     * @param querys
     * @return
     * @throws IOException
     */
    public static HttpResponse doDelete(String host, String path,
                                        Map<String, String> headers,
                                        Map<String, String> querys)
            throws IOException {
        HttpClient httpClient = wrapClient(host);

        HttpDelete request = new HttpDelete(buildUrl(host, path, querys));
        for (Map.Entry<String, String> e : headers.entrySet()) {
            request.addHeader(e.getKey(), e.getValue());
        }

        return httpClient.execute(request);
    }

    private static String buildUrl(String host, String path, Map<String, String> querys) throws UnsupportedEncodingException {
        StringBuilder sbUrl = new StringBuilder();
        sbUrl.append(host);
        if (!StringUtils.isBlank(path)) {
            sbUrl.append(path);
        }
        if (null != querys) {
            StringBuilder sbQuery = new StringBuilder();
            for (Map.Entry<String, String> query : querys.entrySet()) {
                if (0 < sbQuery.length()) {
                    sbQuery.append("&");
                }
                if (StringUtils.isBlank(query.getKey()) && !StringUtils.isBlank(query.getValue())) {
                    sbQuery.append(query.getValue());
                }
                if (!StringUtils.isBlank(query.getKey())) {
                    sbQuery.append(query.getKey());
                    if (!StringUtils.isBlank(query.getValue())) {
                        sbQuery.append("=");
                        sbQuery.append(URLEncoder.encode(query.getValue(), "utf-8"));
                    }
                }
            }
            if (0 < sbQuery.length()) {
                sbUrl.append("?").append(sbQuery);
            }
        }

        return sbUrl.toString();
    }

    private static HttpClient wrapClient(String host) {
        HttpClient httpClient = HttpClients.createDefault();
        return httpClient;
    }

    private static void sslClient(HttpClient httpClient) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] xcs, String str) {

                }
                public void checkServerTrusted(X509Certificate[] xcs, String str) {

                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            ClientConnectionManager ccm = httpClient.getConnectionManager();
            SchemeRegistry registry = ccm.getSchemeRegistry();
            registry.register(new Scheme("https", 443, ssf));
        } catch (KeyManagementException ex) {
            throw new RuntimeException(ex);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }


}
