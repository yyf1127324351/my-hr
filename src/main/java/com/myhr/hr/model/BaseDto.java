package com.myhr.hr.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class BaseDto implements Serializable {

    /*数据库表基本字段*/
    private String remark;
    private Integer deleted;
    private Long createUser;
    private String createUserName;
    private Date createTime;
    private Long updateUser;
    private String updateUserName;
    private Date updateTime;

}
