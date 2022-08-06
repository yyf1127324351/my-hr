package com.myhr.hr.mapper;

import com.myhr.hr.model.UserRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserRoleMapper {
    int deletedUserRoleByUserId(@Param("userId") Long userId);

    int insertBatchUserRole(@Param("list") List<UserRoleDto> list);

    List<UserRoleDto> getRoleByUserId(@Param("userIds") List<Long> userIds);
}
