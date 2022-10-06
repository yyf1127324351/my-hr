package com.myhr.hr.controller.userManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.service.userManage.UserService;
import com.myhr.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @RequestMapping("/updateUserInfo")
    @ResponseBody
    public BaseResponse updateUserInfo(UserDto userDto) {
        try {
            return userService.updateUserInfo(userDto, SessionContainer.getUserId());
        } catch (Exception e) {
            log.error("updateUserInfoException:" + e.getMessage(), e);
            return BaseResponse.error("操作异常");
        }
    }

    /**
     * 人员信息管理-编辑人员信息
     * */
    @RequestMapping("/goUserInfoEditPage")
    public ModelAndView goUserInfoEditPage(@RequestParam("id") Long id) {
        ModelAndView mv = new ModelAndView("/userManage/userInfoEditPage");
        String queryDate = DateUtil.getTodayDate();
        mv.addObject("id", id.toString());
        mv.addObject("queryDate", queryDate);
        return mv;
    }

    /**
     * 人员信息管理-编辑人员信息-跳转到具体编辑页面
     * @param pageType 信息类型
     * @param id 员工id
     * @param queryDate 查询日期
     * */
    @RequestMapping("/goEditInfoPage")
    public ModelAndView goEditInfoPage(@RequestParam("pageType") String pageType,
                                    @RequestParam("id") Long id,
                                    @RequestParam(value = "queryDate", required = true) String queryDate) {

        ModelAndView mv = new ModelAndView();
        Long updateUser = SessionContainer.getUserId();
        //根据需要跳转的页面类型，处理员工对应数据
        userService.handleUserInfo(mv, pageType, id, queryDate,updateUser);
        mv.setViewName("/userManage/edit" + pageType + "Page");
        mv.addObject("id", id);
        return mv;
    }

}
