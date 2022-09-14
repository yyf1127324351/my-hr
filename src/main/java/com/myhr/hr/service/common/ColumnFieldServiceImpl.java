package com.myhr.hr.service.common;

import com.myhr.hr.mapper.ColumnFieldMapper;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldUserDto;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public List<ColumnFieldDto> queryUserColumnField(Long templateId, Long userId) {
        List<ColumnFieldDto> resultList = new ArrayList<>();
        ColumnFieldUserDto filedUser = columnFieldMapper.queryColumnFieldUserTemplateById(templateId);
        if (null != filedUser) {
            String columnFieldIds = filedUser.getColumnFieldIds();
            List<Long> columnFieldIdList = Arrays.asList(columnFieldIds.split(",")).stream().map(s -> Long.valueOf(s)).collect(Collectors.toList());
            List<ColumnFieldDto> list = columnFieldMapper.queryUserColumnField(columnFieldIdList);
            resultList.addAll(list);
        }
        return resultList;
    }

    @Override
    public List<ColumnFieldUserDto> queryColumnFieldUserTemplate(Integer fieldType, Long userId) {
        List<ColumnFieldUserDto> list = columnFieldMapper.queryColumnFieldUser(fieldType,userId);

        list.sort(Comparator.comparing(ColumnFieldUserDto::getIsDefaultShow).reversed()); //倒序排序

        return list;
    }
}
