package com.myhr.hr.model;

import lombok.Data;

/**
 * 菜单表实体类
 * menu表
 * */
@Data
public class MenuDto extends BaseDto {
    private Long id;
    private String name;
    private String code;
    private Integer type;//类型 0菜单 1功能点
    private Long parentId;
    private Integer level;
    private String url;
    private Integer hasChild;
    private Integer sortNumber;
    private Integer deleted;
}
