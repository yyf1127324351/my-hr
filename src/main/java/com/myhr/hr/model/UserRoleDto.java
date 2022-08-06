package com.myhr.hr.model;


import lombok.Data;

import java.util.List;

@Data
public class UserRoleDto extends BaseDto {
    private Long userId;
    private String userName;
    private String loginName;
    private Long roleId;
    private String roleName;


    private List<Long> roleIdList;
    private String userRoleNames;

}
