package com.myhr.hr.mapper;

import com.myhr.hr.model.Operator;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 23:19
 */
@Mapper
@Repository
public interface  OperatorMapper {

    Operator getOperatorById();

}
