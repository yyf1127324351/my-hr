package com.myhr.config;

import lombok.extern.slf4j.Slf4j;
import org.jasig.cas.client.authentication.AuthenticationFilter;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.jasig.cas.client.util.HttpServletRequestWrapperFilter;
import org.jasig.cas.client.validation.Cas30ProxyReceivingTicketValidationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-20 22:32
 */
@Configuration
@Slf4j
@ConditionalOnProperty(value = "cas.loginType", havingValue = "cas")
public class CasFilterConfig {

    /**
     * 需要走cas拦截的地址（/* 所有地址都拦截）
     */
    @Value("${cas.urlPattern}")
    private String filterUrl;
    /**
     * cas不拦截的路径
     */
    @Value("${cas.ignorePattern}")
    private String ignorePattern;


    /**
     * 默认的cas地址
     */
    @Value("${cas.server.loginUrl}")
    private String casServerLoginUrl;

    /**
     * 应用访问地址
     */
    @Value("${cas.server.urlPrefix}")
    private String casServerUrlPrefix;

    @Value("${cas.client.url}")
    private String casClientUrl;

    @Value("${cas.client.login}")
    private String casClientLogin;


    //注册单点登出 Listener
    @Bean
    public ServletListenerRegistrationBean singleSignOutListenerRegistration() {
        log.info(" cas 单点登录配置 casClientUrl = " + casClientUrl + "\n casServerUrlPrefix = " + casServerUrlPrefix);
        log.info(" servletListenerRegistrationBean ");
        ServletListenerRegistrationBean listenerRegistrationBean = new ServletListenerRegistrationBean();
        listenerRegistrationBean.setListener(new SingleSignOutHttpSessionListener());
        listenerRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return listenerRegistrationBean;
    }


    /**
     * 单点登出过滤器
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter() {
        log.info(" servletListenerRegistrationBean ");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new SingleSignOutFilter());
        registrationBean.addUrlPatterns(filterUrl);
        registrationBean.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        registrationBean.setName("CAS Single Sign Out Filter");
        registrationBean.setOrder(1);
        return registrationBean;
    }


    /**
     * 单点登录认证过滤器
     */
    @Bean
    public FilterRegistrationBean AuthenticationFilter() {
        log.info(" AuthenticationFilter ");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new AuthenticationFilter());
        registrationBean.addUrlPatterns(filterUrl);
        registrationBean.setName("CAS Filter");
        registrationBean.addInitParameter("casServerLoginUrl", casServerLoginUrl);
        registrationBean.addInitParameter("serverName", casClientUrl);
        registrationBean.addInitParameter("ignorePattern", ignorePattern);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 单点登录验证过滤器
     */
    @Bean
    public FilterRegistrationBean Cas30ProxyReceivingTicketValidationFilter() {
        log.info(" Cas30ProxyReceivingTicketValidationFilter ");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new Cas30ProxyReceivingTicketValidationFilter());
        registrationBean.addUrlPatterns(filterUrl);
        registrationBean.setName("CAS Validation Filter");
        registrationBean.addInitParameter("casServerUrlPrefix", casServerUrlPrefix);
        registrationBean.addInitParameter("serverName", casClientLogin);
        registrationBean.setOrder(1);
        return registrationBean;
    }

    /**
     * 单点登录请求包装
     */
    @Bean
    public FilterRegistrationBean httpServletRequestWrapperFilter() {
        log.info(" httpServletRequestWrapperFilter ");
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new HttpServletRequestWrapperFilter());
        registrationBean.addUrlPatterns(filterUrl);
        registrationBean.setName("CAS HttpServletRequest Wrapper Filter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

}
