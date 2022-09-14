package com.myhr.hr.model;

import lombok.Data;

//页面动态列表头 实体类

@Data
public class ColumnFieldDto extends BaseDto{
    private Long id; //属性字段id（sys_column_field表id）
    private String field;
    private String name;
    private Integer fieldType;

    private Long roleId;
    private Long columnFieldId;




}
