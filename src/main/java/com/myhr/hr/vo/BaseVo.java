package com.myhr.hr.vo;

import lombok.Data;

import java.util.List;

@Data
public class BaseVo {
    /*数据库表基本字段*/
    private Integer deleted;
    private Long createUser;
    private String createUserName;
    private String createTime;
    private Long updateUser;
    private String updateUserName;
    private String updateTime;


    /*easyui分页字段*/
    private Integer page=1;
    private Integer rows=50;
    private Integer start = (getPage() - 1) * getRows();
    private Integer limit = getRows();

    /*排序*/
    private String sort;
    private String order;

    /*登陆人权限*/
    private List<String> deptAuthorityList;

}
