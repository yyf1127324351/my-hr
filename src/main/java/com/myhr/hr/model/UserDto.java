package com.myhr.hr.model;

import lombok.Data;

/**
 * @Description
 * @Author yyf
 * @Date 2022-07-06 15:41
 */
@Data
public class UserDto {
    private Long id;
    private String userName;
    private String loginName;
    private String companyEmail;
    private Integer status;
    private Long workPlace; //工作地点
    private Long staffType; //员工类型


}
