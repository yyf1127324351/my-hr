package com.myhr.hr.model;

import lombok.Data;

import java.util.List;

/**
 * @Description 列属性类型实体类
 */
@Data
public class ColumnFieldTypeDto extends BaseDto{
    private Integer id;
    private String fieldTypeName;
    private String fieldCode;

    private List<ColumnFieldDto> fieldList;
}
