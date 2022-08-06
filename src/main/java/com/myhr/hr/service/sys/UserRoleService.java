package com.myhr.hr.service.sys;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.UserRoleDto;

import java.util.HashMap;

public interface UserRoleService {
    BaseResponse updateUserRole(UserRoleDto userRoleDto, Long updateUser);

    BaseResponse getUserAccountPageList(HashMap<String, Object> map);
}
