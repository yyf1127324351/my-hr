package com.myhr.hr.service.common;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.ColumnFieldMapper;
import com.myhr.hr.mapper.RoleMapper;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldTemplateUserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
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
    public List<ColumnFieldDto> queryUserColumnField(Long fieldTemplateUserId, Long userId) {
        List<ColumnFieldDto> resultList = new ArrayList<>();
        //获取 该模板选中的列属性 和 非选中的列属性 合集
        List<ColumnFieldDto> allList = handleUserColumnFieldAll(fieldTemplateUserId,userId);
        if (CollectionUtils.isNotEmpty(allList)) {
            //获取模板中选中的列属性
            resultList = allList.stream().filter(e -> e.getHasSelectFlag() == 1).collect(Collectors.toList());
        }
        return resultList;
    }

    @Override
    public BaseResponse queryUserColumnFieldMap(Long fieldTemplateUserId, Long userId) {
        //获取 该模板选中的列属性 和 非选中的列属性 合集
        List<ColumnFieldDto> allList = handleUserColumnFieldAll(fieldTemplateUserId,userId);
        if (CollectionUtils.isNotEmpty(allList)) {
            Map map = new HashMap();
            //获取模板中选中的列属性
            List<ColumnFieldDto> selectList = allList.stream().filter(e -> e.getHasSelectFlag() == 1).collect(Collectors.toList());
            map.put("selectFields", selectList);
            //获取模板中 未选中的列属性
            List<ColumnFieldDto> noSelectList = allList.stream().filter(e -> e.getHasSelectFlag() == 0).collect(Collectors.toList());
            map.put("noSelectFields", noSelectList);
            return BaseResponse.success(map);
        }else {
            return BaseResponse.paramError("该员工无其他列属性权限,无法设置列模板！");
        }
    }

    @Override
    public BaseResponse updateColumnFieldTemplateUser(ColumnFieldTemplateUserDto columnFieldTemplateUserDto, Long updateUser) {

        //校验该模板名称是否存在，如果已经存在则不能保存
        int sameCount = columnFieldMapper.getSameNameOfTemplate(columnFieldTemplateUserDto);
        if (sameCount > 0) {
            return BaseResponse.paramError("该模板名称已存在，无法使用该名称");
        }

        //查出原模板
        ColumnFieldTemplateUserDto oldTemplate = columnFieldMapper.queryColumnFieldTemplateUserById(columnFieldTemplateUserDto.getId());

        columnFieldTemplateUserDto.setUpdateTime(new Date());
        columnFieldTemplateUserDto.setUpdateUser(updateUser);
        //更新
        columnFieldMapper.updateColumnFieldTemplateUser(columnFieldTemplateUserDto);
        //如果 原模板未将该模板设置为默认展示 且本次将该模板设置了默认展示该模板 即 isDefaultShow = 1，
        // 则将该员工，改类型的其他模板isDefaultShow都设置为0
        if (null != oldTemplate && oldTemplate.getIsDefaultShow() == 0 && columnFieldTemplateUserDto.getIsDefaultShow() == 1) {
            columnFieldMapper.updateNeedNotDefaultShow(oldTemplate.getId(),oldTemplate.getUserId(),oldTemplate.getFieldType());
        }

        return BaseResponse.success("更新成功");
    }

    @Override
    public List<ColumnFieldTemplateUserDto> queryColumnFieldTemplateUser(Integer fieldType, Long userId, Integer allFlag) {
        //根据登陆人id和列属性类型查出列展示模板
        List<ColumnFieldTemplateUserDto> list = columnFieldMapper.queryColumnFieldTemplateUser(fieldType,userId,allFlag);

        list.sort(Comparator.comparing(ColumnFieldTemplateUserDto::getIsDefaultShow).reversed()); //倒序排序

        return list;
    }


    public List<ColumnFieldDto> handleUserColumnFieldAll(Long fieldTemplateUserId, Long userId) {
        List<ColumnFieldDto> resultList = new ArrayList<>();
        //员工所选模板
        ColumnFieldTemplateUserDto filedUser = columnFieldMapper.queryColumnFieldTemplateUserById(fieldTemplateUserId);
        if (null != filedUser) {
            //根据登陆人id，获取登陆人的角色，再根据角色获取到 其拥有的列属性
            List<ColumnFieldDto> authFieldList = roleMapper.queryUserRoleColumnFiledList(userId,filedUser.getFieldType());
            if (CollectionUtils.isNotEmpty(authFieldList)) {
                //该员工有用的角色下有权限的 列属性id集合
                List<Long> authFieldIdList = authFieldList.stream().map(ColumnFieldDto :: getId).collect(Collectors.toList());
                Map<Long,ColumnFieldDto> authFieldMap = authFieldList.stream().collect((Collectors.toMap(ColumnFieldDto::getId, Function.identity(), (v1, v2) -> v1)));

                String columnFieldIds = filedUser.getColumnFieldIds();
                //员工所选模板包含的 列属性id集合
                List<Long> columnFieldIdList = Arrays.asList(columnFieldIds.split(",")).stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
                //取交集(模板中已经选中的)
                List<Long> intersectionIds = columnFieldIdList.stream().filter(item -> authFieldIdList.contains(item)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(intersectionIds)) {
                    for (Long id : intersectionIds) {
                        ColumnFieldDto selectField = authFieldMap.get(id);
                        selectField.setHasSelectFlag(1);
                        resultList.add(selectField);
                    }
//                    List<ColumnFieldDto> list = columnFieldMapper.queryUserColumnField(intersectionIds);
//                    resultList.addAll(list);
                }
                //取差集(模板中未选中的) authFieldIdList - columnFieldIdList
                List<Long> noSelectIds = authFieldIdList.stream().filter(item -> !columnFieldIdList.contains(item)).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(noSelectIds)) {
                    for (Long id : noSelectIds) {
                        ColumnFieldDto noSelectField = authFieldMap.get(id);
                        noSelectField.setHasSelectFlag(0);
                        resultList.add(noSelectField);
                    }
//                    List<ColumnFieldDto> list = columnFieldMapper.queryUserColumnField(noSelectIds);
//                    resultList.addAll(list);
                }

            }
        }
        return resultList;
    }

}
