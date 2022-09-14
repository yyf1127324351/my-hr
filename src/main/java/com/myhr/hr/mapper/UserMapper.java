package com.myhr.hr.mapper;

import com.myhr.hr.model.UserRoleDto;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.vo.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
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

    UserDto queryUserById(Long userId);

    Long getUserAccountPageCount(HashMap<String, Object> map);

    List<UserRoleDto> getUserAccountPageList(HashMap<String, Object> map);

    Long getUserInfoPageCount(HashMap<String, Object> map);

    List<UserDto> getUserInfoPageList(HashMap<String, Object> map);
}
