package com.myhr.hr.model;

import lombok.Data;

/**
 * @Description
 * @Author yyf
 * @Date 2022-07-06 15:41
 */
@Data
public class UserDto extends BaseDto{
    private Long id;
    private String userName;
    private String loginName;
    private String companyEmail;
    private Integer status;
    private Long workPlace; //工作地点
    private Long staffType; //员工类型
    private String inductionTeacherId; //入职导师
    private String reportLeaderId; //汇报对象

    private String inductionTeacherName; //入职导师姓名
    private String reportLeaderName; //汇报对象姓名

    private Long userNo;



}
