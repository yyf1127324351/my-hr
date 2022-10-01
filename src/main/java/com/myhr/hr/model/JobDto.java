package com.myhr.hr.model;

import lombok.Data;

/**
 * @Description 岗位实体类
 */
@Data
public class JobDto extends BaseDto {
    private Long id;
    private Long jobId;
    private String jobName;
    private Integer headcount;//编制人数
    private Integer isValid; //是否有效 1有效 0无效
    private Long jobNameId;
    private String startDate;
    private String endDate;


    private Boolean isChangeOperation;



}
