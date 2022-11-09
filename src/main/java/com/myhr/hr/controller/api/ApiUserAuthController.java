package com.myhr.hr.controller.api;

import com.myhr.auth.AuthSystemTool;
import com.myhr.common.BaseResponse;
import com.myhr.hr.service.api.ApiUserAuthService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-21 15:33
 */
@RestController
@RequestMapping("api/v1/userAuth")
public class ApiUserAuthController {

    @Autowired
    AuthSystemTool authSystemTool;
    @Autowired
    ApiUserAuthService apiUserAuthService;

    @GetMapping("/getAuthToken")
    public BaseResponse test() {
        String token = authSystemTool.getToken();
        return BaseResponse.success(token);
    }

    //调用AUTH接口，同步角色权限到AUTH系统，仅初始化一次
    @GetMapping("/userAuthSync")
    public BaseResponse userAuthSync() {
        return apiUserAuthService.userAuthSync();
    }

    //向MQ推送系统的角色
    @GetMapping("/userRoleSync")
    public BaseResponse userRoleSync() {
        return apiUserAuthService.userRoleSync();
    }


}
