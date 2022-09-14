package com.myhr.hr.controller;

import com.myhr.common.SessionContainer;
import com.myhr.common.constant.RedisConstant;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.service.userManage.UserService;
import com.myhr.hr.service.redis.RedisService;
import com.myhr.utils.EncodesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-21 16:28
 */
@Slf4j
@Controller
@RequestMapping("/")
public class LoginController {

    @Value("${cas.server.logoutUrl}")
    private String casServerLogoutUrl;
    @Value("${cas.client.login}")
    private String casClientLogin;
    @Value("${cas.client.index}")
    private String casClientIndex;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @GetMapping(path = {"", "login"})
    public ModelAndView login() {
        return new ModelAndView("redirect:" + casClientIndex);
    }

    //退出登录
    @GetMapping("logout")
    public String logout(HttpServletRequest request) {
        Long userId = SessionContainer.getUserId();
        String userName = SessionContainer.getUserName();
        log.info("用户{}登出时候的用户id:",userName,userId);

        UserDto user = userService.queryUserById(userId);

        redisService.del(RedisConstant.LOGIN_NAME_PREFIX + user.getLoginName());
        redisService.del(RedisConstant.LOGIN_EXPIRED_TIME_PREFIX + user.getLoginName());

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        log.info("用户【{}】登出", userName);

        String returnUrl = casServerLogoutUrl + "?service=" + EncodesUtils.urlEncode(casClientLogin);
        return "redirect:" + returnUrl;
    }

}
