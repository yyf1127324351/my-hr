package com.myhr.hr.service.staffManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.UserMapper;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.model.UserRoleDto;
import com.myhr.hr.service.staffManage.UserService;
import com.myhr.hr.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserVo getUserByMap(Map<String, String> map) {
        return userMapper.getUserByMap(map);
    }

    @Override
    public UserDto queryUserById(Long userId) {
        return userMapper.queryUserById(userId);
    }

    @Override
    public BaseResponse getUserInfoPageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        Long total = userMapper.getUserInfoPageCount(map);
        baseResponse.setTotal(total);
        if (total == 0) {
            return baseResponse;
        }
        List<UserDto> list = userMapper.getUserInfoPageList(map);
        baseResponse.setRows(list);

        return baseResponse;
    }

}
