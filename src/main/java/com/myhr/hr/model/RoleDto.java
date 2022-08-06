package com.myhr.hr.model;

import lombok.Data;

@Data
public class RoleDto extends BaseDto {
    private Long id;
    private String roleCode;
    private String roleName;
    private Integer status;

}
