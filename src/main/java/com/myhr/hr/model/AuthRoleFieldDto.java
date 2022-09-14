package com.myhr.hr.model;

import lombok.Data;

import java.util.List;

/**
 * @Description 角色对应的列属性权限实体类
 */
@Data
public class AuthRoleFieldDto extends BaseDto{
    private Long id;
    private Long roleId;
    private Long columnFieldId;

    private List<Long> addArray;
    private List<Long> deductArray;

}
