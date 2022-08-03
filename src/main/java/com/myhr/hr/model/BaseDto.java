package com.myhr.hr.model;

import lombok.Data;

import java.util.Date;

@Data
public class BaseDto {

    /*数据库表基本字段*/
    private Integer status;
    private String remark;
    private Integer deleted;
    private Long createUser;
    private String createUserName;
    private Date createTime;
    private Long updateUser;
    private String updateUserName;
    private Date updateTime;

    /*登陆人权限*/

}
