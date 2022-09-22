package com.myhr.hr.controller.systemManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldTemplateUserDto;
import com.myhr.hr.service.common.ColumnFieldService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @Description 页面展示列属性字段 controller
 * @DBTable 数据库表 sys_column_field_type,sys_column_field,sys_column_field_template_user
 */

@Slf4j
@Controller
@RequestMapping("/columnField")
public class ColumnFieldController {

    @Autowired
    ColumnFieldService columnFieldService;

    /**
     * 获取员工的展示列模板
     * @param fieldType 列属性类型sys_column_field_type表id   1:员工信息属性 2:工资信息属性
     * @param allFlag 是否查询全部模板（包含通用模板） 0：否;  其他：是
     */
    @RequestMapping("/queryColumnFieldTemplateUser")
    @ResponseBody
    public List<ColumnFieldTemplateUserDto> queryColumnFieldTemplateUser(Integer fieldType,Integer allFlag) {
        Long userId = SessionContainer.getUserId();
        return columnFieldService.queryColumnFieldTemplateUser(fieldType,userId,allFlag);
    }


    /**
     * 获取员工 动态组装列 表头
     * @param fieldTemplateUserId 是sys_column_field_template_user表ID
     */
    @RequestMapping("/queryUserColumnField")
    @ResponseBody
    public List<ColumnFieldDto> queryUserColumnField(Long fieldTemplateUserId) {
        Long userId = SessionContainer.getUserId();
        return columnFieldService.queryUserColumnField(fieldTemplateUserId,userId);
    }

    /**
     * 获取所选模板中  已选择的列属性 和 未选择的列属性
     * @param fieldTemplateUserId 是sys_column_field_template_user表ID
     */
    @RequestMapping("/queryUserColumnFieldMap")
    @ResponseBody
    public BaseResponse queryUserColumnFieldMap(Long fieldTemplateUserId) {
        try {
            Long userId = SessionContainer.getUserId();
            return columnFieldService.queryUserColumnFieldMap(fieldTemplateUserId,userId);
        } catch (Exception e) {
            log.error("queryUserColumnFieldMapException:" + e.getMessage(), e);
            return BaseResponse.error();
        }
    }

    /**
     * 跳转至 设置展示列模板页面
     * @param fieldType 列属性类型sys_column_field_type表id   1:员工信息属性 2:工资信息属性
     * @param fieldTemplateUserId 用户自定义页面展示列模板id; sys_column_field_template_user表id
     * */
    @RequestMapping("/goColumnFieldTemplateEditPage")
    public ModelAndView goColumnFieldTemplateEditPage(Integer fieldType,Integer fieldTemplateUserId){
        ModelAndView mv = new ModelAndView("/common/columnFieldTemplateEditPage");
        mv.addObject("fieldType", fieldType);
        mv.addObject("fieldTemplateUserId", fieldTemplateUserId);
        return mv;
    }

    /**
     * 展示列模板 - 保存
     *
     * */
    @RequestMapping("/updateColumnFieldTemplateUser")
    @ResponseBody
    public BaseResponse updateColumnFieldTemplateUser(ColumnFieldTemplateUserDto columnFieldTemplateUserDto){
        try {
            Long userId = SessionContainer.getUserId();
            return columnFieldService.updateColumnFieldTemplateUser(columnFieldTemplateUserDto,userId);
        } catch (Exception e) {
            log.error("updateColumnFieldTemplateUserException:" + e.getMessage(), e);
            return BaseResponse.error();
        }
    }



}
