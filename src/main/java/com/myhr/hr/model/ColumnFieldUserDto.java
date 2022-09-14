package com.myhr.hr.model;


import lombok.Data;

@Data
public class ColumnFieldUserDto {
    private Long id;
    private Long userId;
    private String fieldType;
    private String templateName;
    private String columnFieldIds;
    private Integer isDefaultShow; //是否默认展示 0否 1是

}
