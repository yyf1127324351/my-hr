package com.myhr.hr.service.common;

import com.myhr.hr.mapper.ColumnFieldMapper;
import com.myhr.hr.mapper.RoleMapper;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldUserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author yyf
 * @Date 2022-08-17 10:40
 */

@Slf4j
@Service
public class ColumnFieldServiceImpl implements ColumnFieldService {

    @Autowired
    ColumnFieldMapper columnFieldMapper;
    @Autowired
    RoleMapper roleMapper;


    @Override
    public List<ColumnFieldDto> queryUserColumnField(Long templateId, Long userId) {
        List<ColumnFieldDto> resultList = new ArrayList<>();
        //员工所选模板
        ColumnFieldUserDto filedUser = columnFieldMapper.queryColumnFieldUserTemplateById(templateId);
        if (null != filedUser) {
            //根据登陆人id，获取登陆人的角色，再根据角色获取到 其拥有的列属性
            List<ColumnFieldDto> authFieldList = roleMapper.queryUserRoleColumnFiledList(userId,filedUser.getFieldType());
            if (CollectionUtils.isNotEmpty(authFieldList)) {
                //该员工有用的角色下有权限的 列属性id集合
                List<Long> authFieldIdList = authFieldList.stream().map(ColumnFieldDto :: getId).collect(Collectors.toList());

                String columnFieldIds = filedUser.getColumnFieldIds();
                //员工所选模板包含的 列属性id集合
                List<Long> columnFieldIdList = Arrays.asList(columnFieldIds.split(",")).stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
                //取交集
                List<Long> intersectionIds = columnFieldIdList.stream().filter(item -> authFieldIdList.contains(item)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(intersectionIds)) {
                    List<ColumnFieldDto> list = columnFieldMapper.queryUserColumnField(intersectionIds);
                    resultList.addAll(list);
                }

            }
        }
        return resultList;
    }

    @Override
    public List<ColumnFieldUserDto> queryColumnFieldUserTemplate(Integer fieldType, Long userId) {
        //根据登陆人id和列属性类型查出列展示模板
        List<ColumnFieldUserDto> list = columnFieldMapper.queryColumnFieldUser(fieldType,userId);

        list.sort(Comparator.comparing(ColumnFieldUserDto::getIsDefaultShow).reversed()); //倒序排序

        return list;
    }
}
