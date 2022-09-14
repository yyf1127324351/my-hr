package com.myhr.hr.mapper;

import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.ColumnFieldTypeDto;
import com.myhr.hr.model.ColumnFieldUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Description
 * @Author yyf
 * @Date 2022-08-17 10:49
 */

@Mapper
@Repository
public interface ColumnFieldMapper {
    List<ColumnFieldUserDto> queryColumnFieldUser(@Param("fieldType") Integer fieldType, @Param("userId") Long userId);

    List<ColumnFieldDto> queryUserColumnField(@Param("columnFieldIdList") List<Long> columnFieldIdList);

    ColumnFieldUserDto queryColumnFieldUserTemplateById(@Param("id") Long id);

    List<ColumnFieldTypeDto> queryAllColumnFiledType(Map<String, Object> paramMap);

    List<ColumnFieldDto> queryAllColumnFiled(Map<String, Object> paramMap);
}
