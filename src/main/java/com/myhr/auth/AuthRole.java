package com.myhr.auth;

import lombok.Data;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-28 16:02
 */

@Data
public class AuthRole {
    private String code;

    private String name;

    private String applicationRoleKey;

    private Integer status;
}


