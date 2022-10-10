package com.myhr.hr.service.systemManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.common.constant.CommonConstant;
import com.myhr.hr.mapper.*;
import com.myhr.hr.model.*;
import com.myhr.hr.service.systemManage.RoleService;
import com.myhr.hr.vo.TreeNode;
import com.myhr.utils.DateUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
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
    @Autowired
    ColumnFieldMapper columnFieldMapper;
    @Autowired
    DepartmentMapper departmentMapper;

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
        List<TreeNode> menuTreeList = TreeNode.convertToTreeList(allMenuList,CommonConstant.TREE_OPEN);
        TreeNode.sortTreeNode(menuTreeList); //菜单排序

        //2.部门权限
        String queryDate = DateUtil.getTodayDate();
        List<TreeNode> allDeptList = departmentMapper.getAllDeptTreeNode(queryDate);
        //角色有权限的地区
        List<RoleAuthorityDto> deptAuthList = allAuthIds.stream().filter(e -> e.getType() == 2).collect(Collectors.toList());
        List<Long> deptIds = deptAuthList.stream().map(RoleAuthorityDto::getAuthId).collect(Collectors.toList());
        TreeNode.isChecked(allDeptList, deptIds); //选中有权限的菜单
        List<TreeNode> deptTreeList = TreeNode.convertToTreeList(allDeptList,CommonConstant.TREE_OPEN);

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

        List<TreeNode> areaTreeList = TreeNode.convertToTreeList(allAreaList,CommonConstant.TREE_OPEN);

        Map<String, List> treeMap = new HashMap<>();
        treeMap.put("menuTreeData", menuTreeList);
        treeMap.put("areaTreeData", areaTreeList);
        treeMap.put("deptTreeData", deptTreeList);

        return treeMap;
    }

    @Override
    public void saveAuthTree(RoleAuthorityDto roleAuthorityDto) {
        List<RoleAuthorityDto> addList = new ArrayList<>();
        List<Long> deleteRoleAuthorityIds = new ArrayList<>();
        Long roleId = roleAuthorityDto.getRoleId();

        //处理菜单权限
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getMenuAuthAdd())) {
            //添加新增的权限
            roleAuthorityDto.getMenuAuthAdd().forEach(authId -> handleAddList(authId,1,roleId,addList));
        }
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getMenuAuthDelete())) {
            //查出对应要删除的 角色权限表的id
            List<Long> ids = roleAuthorityMapper.getRoleAuthorityIdList(roleId,roleAuthorityDto.getMenuAuthDelete(), CommonConstant.ROLE_AUTHORITY_TYPE_MENU);
            deleteRoleAuthorityIds.addAll(ids);
        }

        //处理部门权限
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getDeptAuthAdd())) {
            //添加新增的权限
            roleAuthorityDto.getDeptAuthAdd().forEach(authId -> handleAddList(authId,2,roleId,addList));
        }
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getDeptAuthDelete())) {
            //查出对应要删除的 角色权限表的id
            List<Long> ids = roleAuthorityMapper.getRoleAuthorityIdList(roleId,roleAuthorityDto.getDeptAuthDelete(), CommonConstant.ROLE_AUTHORITY_TYPE_DEPARTMENT);
            deleteRoleAuthorityIds.addAll(ids);
        }

        //处理地区权限
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getAreaAuthAdd())) {
            //添加新增的权限
            roleAuthorityDto.getAreaAuthAdd().forEach(authId -> handleAddList(authId, 3, roleId, addList));
        }
        if (CollectionUtils.isNotEmpty(roleAuthorityDto.getAreaAuthDelete())) {
            //查出对应要删除的 角色权限表的id
            List<Long> ids = roleAuthorityMapper.getRoleAuthorityIdList(roleId,roleAuthorityDto.getAreaAuthDelete(), CommonConstant.ROLE_AUTHORITY_TYPE_AREA);
            deleteRoleAuthorityIds.addAll(ids);
        }

        if (CollectionUtils.isNotEmpty(addList)) {
            roleAuthorityMapper.addRoleAuthority(addList);
        }
        if (CollectionUtils.isNotEmpty(deleteRoleAuthorityIds)) {
            roleAuthorityMapper.deleteRoleAuthority(deleteRoleAuthorityIds);
        }

    }

    @Override
    public List<RoleDto> getRoleList() {
        return roleMapper.getRoleList();
    }

    @Override
    public BaseResponse getRoleField(Long roleId) {
        //获取所有的列属性类型 查询sys_column_field_type表
        Map<String,Object> paramMap = new HashMap();
        List<ColumnFieldTypeDto> filedTypeList = columnFieldMapper.queryAllColumnFiledType(null);
        if (CollectionUtils.isEmpty(filedTypeList)) {
            return BaseResponse.paramError("无列属性类型");
        }
        //查询出所有的列属性字段
        List<ColumnFieldDto> columnFieldList = columnFieldMapper.queryAllColumnFiled(paramMap);
        if (CollectionUtils.isEmpty(columnFieldList)) {
            return BaseResponse.paramError("无列属性字段");
        }
        //对所有列属性进行分组
        Map<Integer, List<ColumnFieldDto>> columnFieldMap = columnFieldList.stream().collect((Collectors.groupingBy(e -> e.getFieldType())));

        //查出该角色权限下的所有的列属性字段
        paramMap.put("roleId", roleId);
        List<ColumnFieldDto> roleColumnFieldList = roleMapper.queryRoleColumnFiled(paramMap);
        Map<Integer, List<ColumnFieldDto>> roleHasColumnFieldMap = roleColumnFieldList.stream().collect((Collectors.groupingBy(e -> e.getFieldType())));

        //返回的的TreeMap
        Map<String, List<TreeNode>> map = new HashMap<>();

        //将对应的列属性关联到列属性类型
        for (ColumnFieldTypeDto fieldTypeDto : filedTypeList) {
            //该属性类型 所有的属性
            List<ColumnFieldDto> fieldDtos = columnFieldMap.get(fieldTypeDto.getId());
            if (CollectionUtils.isNotEmpty(fieldDtos)) {
                fieldTypeDto.setFieldList(fieldDtos);
            }
            //该属性类型，该角色拥有的属性
            List<Long> roleHasFieldIds = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(roleHasColumnFieldMap.get(fieldTypeDto.getId()))) {
                roleHasFieldIds = roleHasColumnFieldMap.get(fieldTypeDto.getId()).stream().map(ColumnFieldDto::getId).collect(Collectors.toList());
            }

            //将改权限下的 列属性权限 处理成树格式
            handleRoleColumnFieldTree(map,fieldTypeDto,fieldDtos,roleHasFieldIds);
        }

        return BaseResponse.success(map);
    }

    @Override
    public BaseResponse updateAuthRoleField(AuthRoleFieldDto authRoleFieldDto, Long updateUser) {
        if (CollectionUtils.isNotEmpty(authRoleFieldDto.getAddArray())) {
            //如果新增的不为空，则新增
            authRoleFieldDto.setCreateUser(updateUser);
            authRoleFieldDto.setCreateTime(new Date());
            roleMapper.insertBatchAuthRoleField(authRoleFieldDto);
        }

        if (CollectionUtils.isNotEmpty(authRoleFieldDto.getDeductArray())) {
            //如果需要删除的不为空，则删除
            authRoleFieldDto.setUpdateUser(updateUser);
            authRoleFieldDto.setUpdateTime(new Date());
            roleMapper.updateBatchAuthRoleField(authRoleFieldDto);
        }

        return BaseResponse.success();
    }

    /**
     * @param treeData 需要处理成的数结构
     * @param fieldTypeDto 列属性类型
     * @param fieldDtos 该列属性类型下的列属性
     * @param roleHasFieldIds 角色拥有的列属性Id
     * */
    private void handleRoleColumnFieldTree(Map<String, List<TreeNode>> treeData, ColumnFieldTypeDto fieldTypeDto, List<ColumnFieldDto> fieldDtos, List<Long> roleHasFieldIds) {

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setId(0L);
        rootTreeNode.setName(fieldTypeDto.getFieldTypeName());
        rootTreeNode.setText(fieldTypeDto.getFieldTypeName());
        rootTreeNode.setCode(fieldTypeDto.getFieldCode());
        //如果该角色拥有的列属性个数等于该属性类型下的列属性个数，则树置位全选状态
        if (CollectionUtils.isNotEmpty(roleHasFieldIds) && roleHasFieldIds.size() == fieldDtos.size()) {
            rootTreeNode.setChecked(true);
        }
        //循环处理子节点
        List<TreeNode> childrenTreeNode = new ArrayList<>();
        for (ColumnFieldDto field : fieldDtos) {
            TreeNode treeNode = new TreeNode();
            treeNode.setId(field.getId());
            treeNode.setName(field.getName());
            treeNode.setText(field.getName());
            if (CollectionUtils.isNotEmpty(roleHasFieldIds) && roleHasFieldIds.contains(field.getId())) {
                treeNode.setChecked(true);
            }
            childrenTreeNode.add(treeNode);
        }
        rootTreeNode.setChildren(childrenTreeNode);

        List<TreeNode> resultList = new ArrayList<>();
        resultList.add(rootTreeNode);

        treeData.put(rootTreeNode.getCode(),resultList);
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
