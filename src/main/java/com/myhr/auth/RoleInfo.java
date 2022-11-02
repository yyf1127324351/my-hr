package com.myhr.auth;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-28 16:01
 */
@Data
public class RoleInfo {

    private String token;

    private List<AuthRole> roles;
}
