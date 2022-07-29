package com.myhr.hr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-21 16:28
 */
@Controller
@RequestMapping("/")
public class LoginController {

    @Value("${cas.server.logoutUrl}")
    private String casServerLogoutUrl;

    @Value("${cas.client.login}")
    private String casClientLogin;

    @Value("${cas.client.index}")
    private String casClientIndex;

    @GetMapping("login")
    public ModelAndView login(){
        return new ModelAndView("redirect:" + casClientIndex);
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        //将session设置为失效
        session.invalidate();
        return "redirect:" + casServerLogoutUrl + "?service=" + casClientLogin;
    }
}
