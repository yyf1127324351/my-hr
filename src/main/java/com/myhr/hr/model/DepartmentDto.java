package com.myhr.hr.model;

import lombok.Data;

/**
 * @Description 部门实体类
 */
@Data
public class DepartmentDto extends BaseDto{
    private Long pkid;
    private Long id;
    private String name;
    private Long parentId;
    private Integer level;
    private Integer hasChild;
    private Long leaderId;
    private String path;
    private String pathName;
    private String startDate;
    private String endDate;
    private Integer sortNumber;


}
