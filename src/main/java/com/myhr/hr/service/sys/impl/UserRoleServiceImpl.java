package com.myhr.hr.service.sys.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.UserMapper;
import com.myhr.hr.mapper.UserRoleMapper;
import com.myhr.hr.model.UserRoleDto;
import com.myhr.hr.service.sys.UserRoleService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 用户角色实现类
 */

@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @Override
    public BaseResponse updateUserRole(UserRoleDto userRoleDto, Long updateUser) {
        Long userId = userRoleDto.getUserId();
        if (null == userId) {
            return BaseResponse.paramError("用户id不能为空");
        }
        if (CollectionUtils.isNotEmpty(userRoleDto.getRoleIdList())) {
            //逻辑删除该员工的所有角色
            userRoleMapper.deletedUserRoleByUserId(userId);
            List<UserRoleDto> userRoles = new ArrayList<>();
            //插入员工角色
            List<Long> roleIds = userRoleDto.getRoleIdList();
            for (Long roleId : roleIds) {
                UserRoleDto userRole = new UserRoleDto();
                userRole.setUserId(userRoleDto.getUserId());
                userRole.setRoleId(roleId);
                userRole.setCreateTime(new Date());
                userRole.setCreateUser(updateUser);
                userRoles.add(userRole);
            }
            userRoleMapper.insertBatchUserRole(userRoles);

        }
        return BaseResponse.success("配置角色成功");
    }

    @Override
    public BaseResponse getUserAccountPageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        Long total = userMapper.getUserAccountPageCount(map);
        baseResponse.setTotal(total);
        if (total == 0) {
            return baseResponse;
        }
        List<UserRoleDto> list = userMapper.getUserAccountPageList(map);

        List<Long> userIds = list.stream().map(e -> e.getUserId()).collect(Collectors.toList());
        //查出这些人的所有权限，并根据 userId分组
        List<UserRoleDto> userRoles =  userRoleMapper.getRoleByUserId(userIds);
        if (CollectionUtils.isNotEmpty(userRoles)) {
            //进行分组
            Map<Long,List<UserRoleDto>> roleMap = userRoles.stream().collect(Collectors.groupingBy(e -> e.getUserId()));
            //遍历员工 并处理员工的角色
            for (UserRoleDto userRoleDto : list) {
                List<UserRoleDto> userRoleList = roleMap.get(userRoleDto.getUserId());
                if (CollectionUtils.isNotEmpty(userRoleList)) {
                    //将员工对应的所有的 角色名称拼接成字符串
                    String userRoleNames = userRoleList.stream().map(c -> c.getRoleName()).collect(Collectors.joining(","));
                    //将员工对应的所有的 角色id放置数组，用于前端辉县角色下拉框使用
                    List<Long> userRoleIds = userRoleList.stream().map(e -> e.getRoleId()).collect(Collectors.toList());
                    userRoleDto.setUserRoleNames(userRoleNames);
                    userRoleDto.setRoleIdList(userRoleIds);
                }
            }
        }
        baseResponse.setRows(list);

        return baseResponse;
    }
}
