package com.myhr.hr.controller.systemManage;

import com.myhr.common.SessionContainer;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldUserDto;
import com.myhr.hr.service.common.ColumnFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Description 页面展示列属性字段 controller
 * @DBTable 数据库表 sys_column_field_type,sys_column_field,sys_column_field_user
 */

@Slf4j
@Controller
@RequestMapping("/columnField")
public class ColumnFieldController {

    @Autowired
    ColumnFieldService columnFieldService;


    /**
     * 获取员工 动态组装列 表头
     */
    @RequestMapping("/queryUserColumnField")
    @ResponseBody
    public List<ColumnFieldDto> queryUserColumnField(Long templateId) {
        Long userId = SessionContainer.getUserId();
        return columnFieldService.queryUserColumnField(templateId,userId);
    }

    /**
     * 获取员工的展示列模板
     */
    @RequestMapping("/queryColumnFieldUserTemplate")
    @ResponseBody
    public List<ColumnFieldUserDto> queryColumnFieldUserTemplate(Integer fieldType) {
        Long userId = SessionContainer.getUserId();
        return columnFieldService.queryColumnFieldUserTemplate(fieldType,userId);
    }


}
