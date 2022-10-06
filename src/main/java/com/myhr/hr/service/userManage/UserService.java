package com.myhr.hr.service.userManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.vo.UserVo;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public interface UserService {
    UserVo getUserByMap(Map<String, String> loginName);

    UserDto queryUserById(Long userId);

    BaseResponse getUserInfoPageList(HashMap<String, Object> map);

    BaseResponse updateUserInfo(UserDto userDto, Long updateUser);

    void handleUserInfo(ModelAndView mv, String pageType, Long id, String queryDate, Long updateUser);
}
