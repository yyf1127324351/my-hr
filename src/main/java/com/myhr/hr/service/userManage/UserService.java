package com.myhr.hr.service.userManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.vo.UserVo;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    UserVo getUserByMap(Map<String, String> loginName);

    UserDto queryUserById(Long userId);

    BaseResponse getUserInfoPageList(HashMap<String, Object> map);
}
