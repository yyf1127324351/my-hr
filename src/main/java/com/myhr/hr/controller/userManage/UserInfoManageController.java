package com.myhr.hr.controller.userManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.service.userManage.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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

    @RequestMapping("/queryUserSearch")
    @ResponseBody
    public BaseResponse queryUserSearch(HttpServletRequest request) {
        HashMap<String, Object> map = getParametersMap(request);
        return userService.getUserInfoPageList(map);
    }


    /**
     * 员工信息管理-编辑
     * */
    @RequestMapping("/saveOrUpdateUserInfo")
    @ResponseBody
    public BaseResponse saveOrUpdateUserInfo(UserDto userDto) {
        if (userDto.getInductionTeacherId() == null || StringUtils.isBlank(userDto.getReportLeaderId())) {
            return BaseResponse.paramError("参数不正确！");
        }
        try {
            return userService.saveOrUpdateUserInfo(userDto, SessionContainer.getUserId());
        } catch (Exception e) {
            log.error("saveOrUpdateUserInfoException:" + e.getMessage(), e);
            return BaseResponse.error("操作异常");
        }
    }


}
