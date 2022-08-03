package com.myhr.hr.mapper;

import com.myhr.hr.model.RoleAuthorityDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Mapper
@Repository
public interface RoleAuthorityMapper {
    List<RoleAuthorityDto> getMenuIdsByRoleId(@Param("roleId") Long roleId);

    void addRoleAuthority(@Param("list") List<RoleAuthorityDto> list);

    void deleteRoleAuthority(@Param("list") List<RoleAuthorityDto> list);
}
