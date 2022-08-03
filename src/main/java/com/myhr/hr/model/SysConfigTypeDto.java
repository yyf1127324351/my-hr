package com.myhr.hr.model;

import lombok.Data;

/**
 * 系统参数类型实体类
 * sys_config_type表
 */
@Data
public class SysConfigTypeDto extends BaseDto {
    private Long id;
    private String typeCode; //类型编码
    private String name; //名称
    private Integer sortNumber; //排序值
}
