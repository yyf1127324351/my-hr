package com.myhr.hr.vo;

import lombok.Data;

@Data
public class UserVo {
    private Long id;
    private Long userId;//人员ID
    private String userName;//中文名
    private String loginName;//域帐户
    private String password;// 密码



}
