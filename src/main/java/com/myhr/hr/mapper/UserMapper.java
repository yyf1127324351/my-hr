package com.myhr.hr.mapper;

import com.myhr.hr.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 23:19
 */
@Mapper
@Repository
public interface UserMapper {

    UserVo getUserByMap(Map<String, String> map);
}
