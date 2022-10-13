package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.common.constant.CommonConstant;
import com.myhr.hr.mapper.DepartmentMapper;
import com.myhr.hr.model.DepartmentDto;
import com.myhr.hr.service.organizationManage.DepartmentService;
import com.myhr.hr.vo.TreeNode;
import com.myhr.utils.DateUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * @Description
 */
@Transactional
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;


    @Override
    public List<TreeNode> getDepartmentTree(String queryDate, Long operateUser) {
        Map<String, Object> map = new HashMap<>();
        map.put("queryDate", queryDate);

        List<TreeNode> treeList = new ArrayList<>();
        //处理顶级部门
        TreeNode top = this.handleTopDepartment();
        top.setState(CommonConstant.TREE_OPEN);
        //处理其他部门
        List<DepartmentDto> deptList = departmentMapper.queryDepartmentList(map);
        if (CollectionUtils.isNotEmpty(deptList)) {
            List<TreeNode> list = this.handleDepartmentToTreeNode(deptList);
            List<TreeNode> childList = TreeNode.convertToTreeList(list, CommonConstant.TREE_CLOSE);
            top.setChildren(childList);
        }
        treeList.add(top);
        return treeList;
    }

    @Override
    public BaseResponse queryDepartmentPageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        Long total = departmentMapper.queryDepartmentPageCount(map);
        baseResponse.setTotal(total);
        if (total == 0) {
            baseResponse.setRows(Collections.EMPTY_LIST);
            return baseResponse;
        }
        List<DepartmentDto> list = departmentMapper.queryDepartmentPageList(map);
        String queryDate = map.get("queryDate").toString();
        for (DepartmentDto dto : list) {
            if (DateUtil.isBefore(dto.getEndDate(), queryDate)) {
                dto.setIsValid(0);
            }
        }
        baseResponse.setRows(list);

        return baseResponse;
    }

    @Override
    public BaseResponse addDepartment(DepartmentDto departmentDto, Long operateUser) {
        //校验该部门的父部门下，是否存在相同部门，如果存在，则返回
        BaseResponse checkResult = checkDepartmentIsExist(departmentDto);
        if (checkResult.getCode() != 200) {
            return checkResult;
        }

        //查询父部门信息
        DepartmentDto parentDept = departmentMapper.queryDepartmentById(departmentDto.getParentId(), DateUtil.getTodayDate());
        if (null == parentDept) {
            return BaseResponse.paramError("未查到该部门的父部门,无法新增该部门");
        }else {
            departmentDto.setParentId(parentDept.getId());
        }

        //插入新部门
        departmentDto.setCreateUser(operateUser);
        departmentDto.setCreateTime(new Date());
        departmentMapper.insertDepartment(departmentDto);
        //根据pkid查出部门数据
        DepartmentDto dept = departmentMapper.queryDepartmentByPkid(departmentDto.getPkid());
        //更新新部门的path
        dept.setPath(parentDept.getPath() + ">" + dept.getId());
        dept.setPathName(parentDept.getPathName() + ">" + departmentDto.getName());
        dept.setUpdateTime(new Date());
        dept.setUpdateUser(operateUser);
        departmentMapper.updateDepartmentByPkid(dept);

        //判断父部门是否已经有子部门了，如果没有子部门，则此时将 has_child字段更新为1，即有子部门
        if (null != parentDept.getHasChild() && parentDept.getHasChild() == 0) {
            parentDept.setHasChild(1);
            parentDept.setUpdateTime(new Date());
            parentDept.setUpdateUser(operateUser);
            departmentMapper.updateDepartmentByPkid(parentDept);
        }


        return BaseResponse.success("新增部门成功", dept);
    }

    @Override
    public BaseResponse expireDepartment(DepartmentDto paramDept, Long operateUser) {
        //1获取当前部门信息
        DepartmentDto dept = departmentMapper.queryDepartmentByPkid(paramDept.getPkid());

        //2.获取所有子部门
        List<DepartmentDto> childrenList = departmentMapper.queryAllChildrenDepartment(dept.getPath(), paramDept.getEndDate());



        //3.判断所有子部门下是否还有未离职的员工

        //4.如果部门下无员工，则进行部门，和子部门失效，

        //5.将失效的部门和子部门下的岗位进行失效



        return BaseResponse.success("已完成对该部门以及其子部门的失效");
    }

    private BaseResponse checkDepartmentIsExist(DepartmentDto departmentDto) {
        if (DateUtil.isBefore(departmentDto.getEndDate(), departmentDto.getStartDate())) {
            return BaseResponse.paramError("失效日期不能早于生效日期");
        }
        if (DateUtil.isBefore(departmentDto.getEndDate(), DateUtil.getTodayDate())) {
            return BaseResponse.paramError("失效日期不能早于当前日期");
        }

        int count = departmentMapper.querySameDepartmentCount(departmentDto);
        if (count > 0) {
            return BaseResponse.paramError("该部门已经存在,无法继续操作");
        }
        return BaseResponse.success();
    }

    private TreeNode handleTopDepartment() {
        DepartmentDto departmentDto = departmentMapper.queryTopDepartment();
        if (null != departmentDto) {
            return this.departmentConvertToTreeNode(departmentDto);
        }
        return new TreeNode();
    }

    private List<TreeNode> handleDepartmentToTreeNode(List<DepartmentDto> deptList) {
        List<TreeNode> treeNodeList = new ArrayList<>();
        for (DepartmentDto dept : deptList) {
            TreeNode treeNode = this.departmentConvertToTreeNode(dept);
            treeNodeList.add(treeNode);
        }
        return treeNodeList;
    }

    private TreeNode departmentConvertToTreeNode(DepartmentDto dept) {
        TreeNode treeNode = new TreeNode();
        treeNode.setId(dept.getId());
        treeNode.setPkid(dept.getPkid());
        treeNode.setName(dept.getName());
        treeNode.setText(dept.getName());
        treeNode.setLevel(dept.getLevel());
        treeNode.setParentId(dept.getParentId());
        treeNode.setHasChild(dept.getHasChild());
        treeNode.setStartDate(dept.getStartDate());
        treeNode.setEndDate(dept.getEndDate());
        return treeNode;
    }
}
