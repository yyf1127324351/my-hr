package com.myhr.hr.controller.userManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.service.staffManage.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Description 员工信息管理controller
 * @DBTable 数据库表 user
 */
@Slf4j
@Controller
@RequestMapping("/userInfo")
public class UserInfoManageController extends BaseController{

    @Autowired
    UserService userService;

    //员工管理页面
    @RequestMapping("/goUserInfoPage")
    public ModelAndView goUserAccountPage(){
        return new ModelAndView("/userManage/userInfoPage");
    }

    /**
     * 员工管理列表
     * */
    @RequestMapping("/getUserInfoPageList")
    @ResponseBody
    public BaseResponse getUserInfoPageList(HttpServletRequest request){
        try {
            HashMap<String, Object> map = getParametersMap(request);
            return userService.getUserInfoPageList(map);
        }catch (Exception e){
            log.error("getUserInfoPageListException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }

}
