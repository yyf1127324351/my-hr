package com.myhr.hr.service.sys.impl;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.mapper.MenuMapper;
import com.myhr.hr.mapper.RoleAuthorityMapper;
import com.myhr.hr.mapper.RoleMapper;
import com.myhr.hr.mapper.SysConfigMapper;
import com.myhr.hr.model.RoleAuthorityDto;
import com.myhr.hr.model.RoleDto;
import com.myhr.hr.service.sys.RoleService;
import com.myhr.hr.vo.TreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;
    @Autowired
    MenuMapper menuMapper;
    @Autowired
    RoleAuthorityMapper roleAuthorityMapper;
    @Autowired
    SysConfigMapper sysConfigMapper;

    @Override
    public BaseResponse getRolePageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setTotal(roleMapper.getRolePageCount(map));
        List<RoleDto> list = roleMapper.getRolePageList(map);
        baseResponse.setRows(list);
        return baseResponse;
    }

    @Override
    public void updateRole(RoleDto roleDto) {
        roleDto.setUpdateUser(SessionContainer.getUserId());
        roleDto.setUpdateTime(new Date());
        roleMapper.updateRole(roleDto);
    }

    @Override
    public void addRole(RoleDto roleDto) {
        roleDto.setCreateUser(SessionContainer.getUserId());
        roleMapper.addRole(roleDto);
    }

    @Override
    public Map<String, List> getAuthTree(Long roleId) {

        //查出该角色有权限的菜单，部门，地区，
        List<RoleAuthorityDto> allAuthIds = roleAuthorityMapper.getMenuIdsByRoleId(roleId);

        //1.菜单权限
        //查出所有的菜单
        List<TreeNode> allMenuList = menuMapper.getAllMenuTreeNode();
        //角色有权限的菜单
        List<RoleAuthorityDto> menuAuthList = allAuthIds.stream().filter(e -> e.getType() == 1).collect(Collectors.toList());
        List<Long> menuIds = menuAuthList.stream().map(RoleAuthorityDto::getAuthId).collect(Collectors.toList());
        TreeNode.isChecked(allMenuList, menuIds); //选中有权限的菜单
        List<TreeNode> menuTreeList = TreeNode.convertToTreeList(allMenuList,"open");
        TreeNode.sortTreeNode(menuTreeList); //菜单排序
        //2.部门权限

        //3.地区权限
        //查出所有的地区
        List<TreeNode> allAreaList = sysConfigMapper.getAllAreaTreeNode();
        //角色有权限的地区
        List<RoleAuthorityDto> areaAuthList = allAuthIds.stream().filter(e -> e.getType() == 3).collect(Collectors.toList());
        List<Long> areaIds = areaAuthList.stream().map(RoleAuthorityDto::getAuthId).collect(Collectors.toList());
        TreeNode.isChecked(allAreaList, areaIds);
        //插入所有地区root节点
        TreeNode rootArea = new TreeNode(0L, "所有地区");
        int checkedCount = (int) allAreaList.stream().filter(TreeNode::getChecked).count();
        //如果权限是所有地区，则选中父节点
        if (allAreaList.size() == checkedCount) {
            rootArea.setChecked(true);
        }
        allAreaList.add(rootArea);

        List<TreeNode> areaTreeList = TreeNode.convertToTreeList(allAreaList,"open");

        Map<String, List> treeMap = new HashMap<>();
        treeMap.put("menuTreeData", menuTreeList);
        treeMap.put("areaTreeData", areaTreeList);

        return treeMap;
    }

    @Override
    public void saveAuthTree(RoleAuthorityDto roleAuthorityDto) {
        List<RoleAuthorityDto> addList = new ArrayList<>();
        List<RoleAuthorityDto> deleteList = new ArrayList<>();
        Long roleId = roleAuthorityDto.getRoleId();

        //处理菜单权限
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getMenuAuthAdd())) {
            //添加新增的权限
            roleAuthorityDto.getMenuAuthAdd().forEach(authId -> handleAddList(authId,1,roleId,addList));
        }
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getMenuAuthDelete())) {
            //删除去掉的权限
            roleAuthorityDto.getMenuAuthDelete().forEach(authId -> handleDeleteList(authId, 1, roleId, deleteList));
        }

        //处理地区权限
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getAreaAuthAdd())) {
            //添加新增的权限
            roleAuthorityDto.getAreaAuthAdd().forEach(authId -> handleAddList(authId, 3, roleId, addList));
        }
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getAreaAuthDelete())) {
            //删除去掉的权限
            roleAuthorityDto.getAreaAuthDelete().forEach(authId -> handleDeleteList(authId, 3, roleId, deleteList));
        }

        if (CollectionUtils.isNotEmpty(addList)) {
            roleAuthorityMapper.addRoleAuthority(addList);
        }
        if (CollectionUtils.isNotEmpty(deleteList)) {
            roleAuthorityMapper.deleteRoleAuthority(deleteList);
        }
        //查出拥有该角色的所有的用户




    }

    @Override
    public List<RoleDto> getRoleList() {
        return roleMapper.getRoleList();
    }

    private void handleAddList(Long authId, int type, Long roleId, List<RoleAuthorityDto> addList) {
        RoleAuthorityDto dto = new RoleAuthorityDto();
        dto.setType(type);
        dto.setRoleId(roleId);
        dto.setAuthId(authId);
        dto.setCreateUser(SessionContainer.getUserId());
        dto.setCreateTime(new Date());
        addList.add(dto);
    }

    private void handleDeleteList(Long authId, int type, Long roleId, List<RoleAuthorityDto> deleteList) {
        RoleAuthorityDto dto = new RoleAuthorityDto();
        dto.setType(type);
        dto.setRoleId(roleId);
        dto.setAuthId(authId);
        deleteList.add(dto);
    }
}
