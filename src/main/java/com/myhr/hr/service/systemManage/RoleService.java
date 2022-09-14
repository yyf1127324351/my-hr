package com.myhr.hr.service.systemManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.AuthRoleFieldDto;
import com.myhr.hr.model.RoleAuthorityDto;
import com.myhr.hr.model.RoleDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RoleService {
    BaseResponse getRolePageList(HashMap<String, Object> map);

    void updateRole(RoleDto roleDto);

    void addRole(RoleDto roleDto);

    Map<String,List> getAuthTree(Long roleId);

    void saveAuthTree(RoleAuthorityDto roleAuthorityDto);

    List<RoleDto> getRoleList();

    BaseResponse getRoleField(Long roleId);

    BaseResponse updateAuthRoleField(AuthRoleFieldDto authRoleFieldDto, Long updateUser);
}
