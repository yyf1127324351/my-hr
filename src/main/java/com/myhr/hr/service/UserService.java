package com.myhr.hr.service;

import com.myhr.hr.model.UserDto;
import com.myhr.hr.vo.UserVo;

import java.util.Map;

public interface UserService {
    UserVo getUserByMap(Map<String, String> loginName);

    UserDto queryUserById(Long userId);

}
