package com.myhr.utils.cas;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


public final class HttpUtil {

    private HttpUtil() {

    }

    private static final int DEFAULT_CONNECT_TIMEOUT = 500;

    private static final int DEFAULT_READ_TIMEOUT = 5000;

    private static final String GET_METHOD = "GET";

    private static final String POST_METHOD = "POST";

    private static final String DELETE_METHOD = "DELETE";

    public static HttpURLConnection openGetConnection(final URL url) throws IOException {
        return openConnection(url, GET_METHOD);
    }

    public static HttpURLConnection openPostConnection(final URL url) throws IOException {
        return openConnection(url, POST_METHOD);
    }

    public static HttpURLConnection openDeleteConnection(final URL url) throws IOException {
        return openConnection(url, DELETE_METHOD);
    }

    private static HttpURLConnection openConnection(final URL url, final String method) throws IOException {
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod(method);
        connection.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        connection.setReadTimeout(DEFAULT_READ_TIMEOUT);
        return connection;
    }

    public static String encodeQueryParam(final String paramName, final String paramValue) {
        return urlEncode(paramName) + "=" + urlEncode(paramValue);
    }

    private static String urlEncode(final String text) {
        try {
            return URLEncoder.encode(text, StandardCharsets.UTF_8.name());
        } catch (final UnsupportedEncodingException e) {
            final String message = "Unable to encode text : " + text;
            throw new IllegalArgumentException(message, e);
        }
    }

    public static String getBody(HttpURLConnection connection) {
        InputStream is;
        try {
            is = (InputStream) connection.getContent();
            int size = is.available();
            if (size > 0) {
                byte[] bytes = new byte[size];
                is.read(bytes);
                return new String(bytes);
            }
        } catch (IOException e) {
//            throw new ClientException("get body failed", e);
        }
        return null;
    }
}
