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
    private Long hrbpId;
    private String path;
    private String pathName;
    private String startDate; //部门生效日期
    private String endDate; //部门失效日期
    private String introduction;//部门介绍
    private Integer sortNumber;

    private Integer isValid; //是否有效 1有效 0无效
    private String queryDate;


}
