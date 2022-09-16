package com.myhr.hr.model;


import lombok.Data;

@Data
public class ColumnFieldTemplateUserDto {
    private Long id;
    private Long userId;
    private Integer fieldType;
    private String templateName;
    private String columnFieldIds;
    private Integer isDefaultShow; //是否默认展示 0否 1是

}
