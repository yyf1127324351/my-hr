package com.myhr.hr.model;

import lombok.Data;

import java.util.List;

@Data
public class RoleAuthorityDto extends BaseDto{
    private Long id;
    private Long roleId;
    private Long authId;
    private Integer type;

    private List<Long> menuAuthAdd;
    private List<Long> menuAuthDelete;
    private List<Long> areaAuthAdd;
    private List<Long> areaAuthDelete;


}
