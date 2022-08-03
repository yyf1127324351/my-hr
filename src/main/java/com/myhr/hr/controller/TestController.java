package com.myhr.hr.controller;

import com.myhr.common.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-21 15:50
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/test")
    public BaseResponse test() {
        return BaseResponse.success("success");
    }
}
