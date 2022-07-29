package com.myhr.hr.controller;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 23:15
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    //首页
    @GetMapping("")
    public ModelAndView goIndex(HttpServletRequest request){
        Assertion assertion = (Assertion) request.getSession().getAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        String loginName = null;
        if (assertion != null) {
            AttributePrincipal principal = assertion.getPrincipal();
            loginName = principal.getName();
        }
        ModelAndView mv=new ModelAndView("index");//模板文件的名称，不需要指定后缀
        mv.addObject("loginName",loginName);
        return mv;
    }

}
