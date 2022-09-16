package com.myhr.hr.service.common;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldTemplateUserDto;

import java.util.List;

public interface ColumnFieldService {

    /**
     * @Description 查询需要展示的类字段
     * @param fieldTemplateUserId 列模板的id,即sys_column_field_template_user表id
     * @param userId 登陆人id
     * */
    List<ColumnFieldDto> queryUserColumnField(Long fieldTemplateUserId, Long userId);

    /**
     * @Description 查询该员工的列字段模板
     * @param fieldType 列字段类型 1人员信息 2薪资
     * @param userId 登陆人id
     * @param allFlag   */
    List<ColumnFieldTemplateUserDto> queryColumnFieldTemplateUser(Integer fieldType, Long userId, Integer allFlag);

    /**
     * @Description  获取所选模板中  已选择的列属性 和 未选择的列属性
     * @param fieldTemplateUserId 列模板的id,即sys_column_field_template_user表id
     * @param userId 登陆人id
     * */
    BaseResponse queryUserColumnFieldMap(Long fieldTemplateUserId, Long userId);
}
