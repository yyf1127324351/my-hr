package com.myhr.hr.service.common;

import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldUserDto;

import java.util.List;

public interface ColumnFieldService {

    /**
     * @Description 查询需要展示的类字段
     * @param templateId 列模板的id,即sys_column_field_user表id
     * @param userId 登陆人id
     * */
    List<ColumnFieldDto> queryUserColumnField(Long templateId, Long userId);

    /**
     * @Description 查询该员工的列字段模板
     * @param fieldType 列字段类型 1人员信息 2薪资
     * @param userId 登陆人id
     * */
    List<ColumnFieldUserDto> queryColumnFieldUserTemplate(Integer fieldType, Long userId);
}
