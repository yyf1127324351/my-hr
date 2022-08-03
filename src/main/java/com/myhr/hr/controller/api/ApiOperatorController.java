package com.myhr.hr.controller.api;

import com.myhr.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-21 15:33
 */
@RestController
@RequestMapping("api/v1/operator")
public class ApiOperatorController {

    @GetMapping("/test")
    public BaseResponse test() {
        return BaseResponse.success("success");
    }
}
