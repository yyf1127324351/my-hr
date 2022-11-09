package com.myhr.hr.mapper;

import com.myhr.auth.AuthRole;
import com.myhr.hr.model.AuthRoleFieldDto;
import com.myhr.hr.model.ColumnFieldDto;
import com.myhr.hr.model.RoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Mapper
@Repository
public interface RoleMapper {
    Long getRolePageCount(HashMap<String, Object> map);

    List<RoleDto> getRolePageList(HashMap<String, Object> map);

    void updateRole(RoleDto roleDto);

    void addRole(RoleDto roleDto);

    List<RoleDto> getRoleList();

    List<ColumnFieldDto> queryRoleColumnFiled(Map<String, Object> paramMap);

    int insertBatchAuthRoleField(@Param("authRoleFieldDto") AuthRoleFieldDto authRoleFieldDto);

    int updateBatchAuthRoleField(@Param("authRoleFieldDto") AuthRoleFieldDto authRoleFieldDto);

    List<ColumnFieldDto> queryUserRoleColumnFiledList(@Param("userId") Long userId, @Param("fieldType") Integer fieldType);

    List<AuthRole> getAllAuthRole();
}
