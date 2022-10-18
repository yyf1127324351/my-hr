package com.myhr.hr.controller.api;

import com.myhr.common.BaseResponse;
import com.myhr.common.constant.RabbitMqConstants;
import com.myhr.rabbitMq.MessageContent;
import com.myhr.rabbitMq.RabbitMqService;
import com.myhr.utils.cas.CasLoginVerify;
import com.myhr.utils.cas.LoginResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        messageContent.setOperateType("ADD");

        rabbitMqService.sendMessage(RabbitMqConstants.AUTH_ROLE_EXCHANGE, "auth_role_queue_test", messageContent);


        return BaseResponse.success("消息发送完成");
    }


    @GetMapping("/casVerify")
    public BaseResponse casVerify(String username, String password) {

        LoginResult loginResult = casLoginVerify.login(username, password);

        return BaseResponse.success(loginResult);
    }

}
