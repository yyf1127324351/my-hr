package com.myhr.hr.service.userManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.UserMapper;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.service.userManage.UserService;
import com.myhr.hr.vo.UserVo;
import com.myhr.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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

    @Override
    public BaseResponse updateUserInfo(UserDto userDto, Long updateUser) {
            //更新
            userDto.setUpdateTime(new Date());
            userDto.setUpdateUser(updateUser);
            userMapper.updateUserInfo(userDto);
            return BaseResponse.success("更新成功");

    }

    @Override
    public void handleUserInfo(ModelAndView mv, String pageType, Long userId, String queryDate, Long updateUser) {
        if (StringUtils.isBlank(queryDate)) {
            queryDate = DateUtil.getTodayDate();
        }

        UserDto userDto = new UserDto();
        //根据查询日期来判断是查询当前用户数据还是查询历史用户数据
        if (DateUtil.isBefore(queryDate, DateUtil.getTodayDate())) {
            //查询日期在今天之前，则查询员工历史表信息

        }else {
            //查询员工当前数据
            userDto = userMapper.queryUserById(userId);
        }
        mv.addObject("userInfo", userDto);









    }

}
