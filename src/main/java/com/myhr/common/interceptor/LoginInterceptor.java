package com.myhr.common.interceptor;

import com.myhr.common.SessionContainer;
import com.myhr.common.constant.RedisConstant;
import com.myhr.hr.model.Operator;
import com.myhr.hr.service.OperatorService;
import com.myhr.hr.service.redis.RedisService;
import com.myhr.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * @Description
 * @Author yyf
 * @Date 2022-07-30 16:43
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Value("${cas.server.loginUrl}")
    private String casServerLoginUrl;

    @Value("${cas.client.login}")
    private String casClientLogin;

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    OperatorService operatorService;
    @Autowired
    private RedisService redisService;

    private static final int EXPIRED_SECOND = 10 * 3600;


    /***
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        SessionContainer.clear();

        String servletPath = request.getServletPath();
        String requestUrl = request.getRequestURL().toString();

        final HttpSession session = request.getSession(false);
        final Assertion assertion = session != null ? (Assertion) session.getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION) : null;
        if(assertion == null || assertion.getPrincipal() == null || StringUtils.isBlank(assertion.getPrincipal().getName())) {
            redirectOrNotice(request, response);
            return false;
        }

        AttributePrincipal principal = assertion.getPrincipal();
        String loginName = principal.getName();

        String expiredTime = (String) (principal.getAttributes().get("expired_time"));
        if(StringUtils.isNotBlank(expiredTime)) {
            Date time = DateUtils.parseDate(expiredTime, "yyyy-MM-dd'T'HH:mm:ss.SSS");
            if(time.compareTo(new Date()) <= 0) {
                redirectOrNotice(request, response);
                return false;
            }
            Date exTime = (Date) redisService.getObjectByKey(RedisConstant.LOGIN_EXPIRED_TIME_PREFIX + loginName);
            if(exTime == null) {
                redisService.setKey_Obj(RedisConstant.LOGIN_EXPIRED_TIME_PREFIX + loginName, time, EXPIRED_SECOND);
            }
        }

        Operator user = (Operator) redisService.getObjectByKey(RedisConstant.LOGIN_NAME_PREFIX + loginName);
        if(user == null) {
            user = operatorService.getOperatorByMap(Collections.singletonMap("loginName", loginName));
            if(user == null) {
                redirectOrNotice(request, response);
                return false;
            }
            redisService.setKey_Obj(RedisConstant.LOGIN_NAME_PREFIX + loginName, user, EXPIRED_SECOND);

            log.info("用户【{}】登入成功！", user.getOperatorName());

        }

        SessionContainer.setSession(user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    private void redirectOrNotice(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String REDIRECT_SSO_LOGIN_URL = casServerLoginUrl + "?service=" + EncodesUtils.urlEncode(casClientLogin);

        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        if(WebUtils.isAjax(request)) {
            response.addHeader("REQUIRES_AUTH", "1");
            response.setStatus(399);
            response.setContentType("application/json; charset=UTF-8");
            JsonUtils.writeJson(Collections.singletonMap("url", REDIRECT_SSO_LOGIN_URL), response.getOutputStream());
        }else {
            WebUtils.redirectUrl(response, REDIRECT_SSO_LOGIN_URL, true);
        }
    }
}
