package com.myhr.hr.service.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.DepartmentDto;
import com.myhr.hr.vo.TreeNode;

import java.util.HashMap;
import java.util.List;

public interface DepartmentService {
    List<TreeNode> getDepartmentTree(String queryDate, Long operateUser);

    BaseResponse queryDepartmentPageList(HashMap<String, Object> map);

    BaseResponse addDepartment(DepartmentDto departmentDto, Long operateUser);
}
