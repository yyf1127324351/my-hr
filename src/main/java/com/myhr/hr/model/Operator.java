package com.myhr.hr.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 23:17
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Operator implements Serializable {
    private Long id;
    private Long operatorId;
    private String operatorName;
    private String operatorLoginName;

}
