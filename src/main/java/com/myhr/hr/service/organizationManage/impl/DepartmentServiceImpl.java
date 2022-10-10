package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.common.constant.CommonConstant;
import com.myhr.hr.mapper.DepartmentMapper;
import com.myhr.hr.model.DepartmentDto;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.service.organizationManage.DepartmentService;
import com.myhr.hr.vo.TreeNode;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * @Description
 */
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

        baseResponse.setRows(list);

        return baseResponse;
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
        treeNode.setName(dept.getName());
        treeNode.setText(dept.getName());
        treeNode.setLevel(dept.getLevel());
        treeNode.setParentId(dept.getParentId());
        treeNode.setHasChild(dept.getHasChild());
        return treeNode;
    }
}
