package com.myhr.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 22:47
 */

@Configuration
public class DruidConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(),"/druid/*");

        //后台需要有人登陆，账号密码配置
        HashMap<String,String> stringHashMap = new HashMap<>();

        //增加配置
        stringHashMap.put("loginUsername","admin");//key值是固定的
        stringHashMap.put("loginPassword","admin");

        //允许谁可以访问
        stringHashMap.put("allow","");

        //禁止谁访问
        stringHashMap.put("lss","");//账号和ip地址

        bean.setInitParameters(stringHashMap);
        return bean;
    }
}
