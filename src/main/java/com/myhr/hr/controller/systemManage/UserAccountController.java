package com.myhr.hr.controller.systemManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.model.UserRoleDto;
import com.myhr.hr.service.systemManage.UserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Description 账号管理controller
 * @DBTable 数据库表 user,sys_auth_user_role
 */

@Slf4j
@Controller
@RequestMapping("/userAccount")
public class UserAccountController extends BaseController {

    @Autowired
    UserRoleService userRoleService;

    //账号管理页面
    @RequestMapping("/goUserAccountPage")
    public ModelAndView goUserAccountPage(){
        return new ModelAndView("/systemManage/userAccountPage");
    }

    /**
     * 账号管理列表
     * */
    @RequestMapping("/getUserAccountPageList")
    @ResponseBody
    public BaseResponse getUserAccountPageList(HttpServletRequest request){
        try {
            HashMap<String, Object> map = getParametersMap(request);
            return userRoleService.getUserAccountPageList(map);
        }catch (Exception e){
            log.error("getUserAccountPageListException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }

    /**
     * 账号管理--配置角色
     */
    @RequestMapping("/updateUserRole")
    @ResponseBody
    private BaseResponse updateUserRole(UserRoleDto userRoleDto){
        try {
            Long updateUser = SessionContainer.getUserId();
            return userRoleService.updateUserRole(userRoleDto,updateUser);
        } catch (Exception e) {
            log.error("updateUserRoleException:" + e.getMessage(), e);
            return BaseResponse.error("配置角色异常");
        }
    }

}
