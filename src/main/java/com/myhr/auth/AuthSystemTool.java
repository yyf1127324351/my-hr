package com.myhr.auth;

import com.myhr.utils.HttpUtils;
import com.myhr.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class AuthSystemTool {

    @Value("${auth.appKey}")
    private String authHrAppKey;

    @Value("${auth.appSecret}")
    private String authHrAppSecret;

    @Value("${auth.tokenUrl}")
    private String authTokenUrl;

    public String getToken() {
        try {
            Application application = new Application();
            application.setAppKey(authHrAppKey);
            application.setAppSecret(authHrAppSecret);
            HttpResponse response = HttpUtils.doPost(authTokenUrl, JsonUtils.toJson(application));
            TokenResult token = HttpUtils.convertResponse(response, TokenResult.class);
            if(token != null) {
                if(token.isSuccess()) {
                    return token.getData();
                }
            }
            return "";
        } catch (IOException e) {
            log.error("获取Auth系统token异常:" + e.getMessage(), e);
            return "";
        }
    }


    public static class Application {

        private String appKey;

        private String appSecret;

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getAppSecret() {
            return appSecret;
        }

        public void setAppSecret(String appSecret) {
            this.appSecret = appSecret;
        }
    }

    public static class TokenResult extends AuthResult<String> { }

}
