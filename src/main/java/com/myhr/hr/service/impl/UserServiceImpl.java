package com.myhr.hr.service.impl;

import com.myhr.hr.mapper.UserMapper;
import com.myhr.hr.service.UserService;
import com.myhr.hr.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public UserVo getUserByMap(Map<String, String> map) {
        return userMapper.getUserByMap(map);
    }
}