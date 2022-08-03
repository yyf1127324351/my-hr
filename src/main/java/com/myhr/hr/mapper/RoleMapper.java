package com.myhr.hr.mapper;

import com.myhr.hr.model.RoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Mapper
@Repository
public interface RoleMapper {
    Long getRolePageCount(HashMap<String, Object> map);

    List<RoleDto> getRolePageList(HashMap<String, Object> map);

    void updateRole(RoleDto roleDto);

    void addRole(RoleDto roleDto);
}
