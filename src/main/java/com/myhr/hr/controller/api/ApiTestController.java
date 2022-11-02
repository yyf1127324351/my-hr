package com.myhr.hr.controller.api;

import com.myhr.auth.AuthRole;
import com.myhr.auth.AuthSystemTool;
import com.myhr.auth.RoleInfo;
import com.myhr.common.BaseResponse;
import com.myhr.common.constant.RabbitMqConstants;
import com.myhr.rabbitMq.MessageContent;
import com.myhr.rabbitMq.RabbitMqService;
import com.myhr.utils.cas.CasLoginVerify;
import com.myhr.utils.cas.LoginResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-13 11:30
 */

@RestController
@RequestMapping("api/v1/test")
@RequiredArgsConstructor
public class ApiTestController {

    private final RabbitMqService rabbitMqService;
    private final CasLoginVerify casLoginVerify;

    @GetMapping("/sendDirectMessage")
    public BaseResponse sendDirectMessage() {
        String msg2 = "测试2";
        rabbitMqService.sendMessage(RabbitMqConstants.AUTH_ROLE_EXCHANGE, "auth_role_queue_test2", msg2);


        String msg = "这是一个测试消息";
        MessageContent messageContent = new MessageContent();
        messageContent.setData(msg);
        messageContent.setOperation("ADD");

        rabbitMqService.sendMessage(RabbitMqConstants.AUTH_ROLE_EXCHANGE, "auth_role_queue_test", messageContent);


        return BaseResponse.success("消息发送完成");
    }


    @GetMapping("/casVerify")
    public BaseResponse casVerify(String username, String password) {

        LoginResult loginResult = casLoginVerify.doCasLoginVerify(username, password);

        return BaseResponse.success(loginResult);
    }

    @Autowired
    AuthSystemTool authSystemTool;

    @GetMapping("/getAuthToken")
    public BaseResponse getAuthToken() {
        String token = authSystemTool.getToken();
        return BaseResponse.success(token);
    }

    @GetMapping("/sendMqMessage")
    public BaseResponse sendMqMessage() {
        String token = authSystemTool.getToken();
        if (StringUtils.isBlank(token)) {
            return BaseResponse.error("未获取到token");
        }


        List<AuthRole> roleList = new ArrayList<>();
        AuthRole role = new AuthRole();
        role.setApplicationRoleKey("1");
        role.setCode("test");
        role.setName("测试角色");
        role.setStatus(1);
        roleList.add(role);

        AuthRole role2 = new AuthRole();
        role2.setApplicationRoleKey("2");
        role2.setCode("test2");
        role2.setName("测试角色2");
        role2.setStatus(1);
        roleList.add(role2);


        MessageContent messageContent = new MessageContent();
        messageContent.setToken(token);
        messageContent.setOperation(RabbitMqConstants.REFRESH_ROLE_INFO);
        messageContent.setData(roleList);

        rabbitMqService.sendMessage(RabbitMqConstants.AUTH_ROLE_EXCHANGE, RabbitMqConstants.AUTH_ROLE_QUEUE_NEW, messageContent);


        return BaseResponse.success();
    }

}
