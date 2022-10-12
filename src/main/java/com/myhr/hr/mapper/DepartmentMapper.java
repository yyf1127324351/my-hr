package com.myhr.hr.mapper;

import com.myhr.hr.model.DepartmentDto;
import com.myhr.hr.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface DepartmentMapper {
    List<DepartmentDto> queryDepartmentList(Map<String, Object> map);

    DepartmentDto queryTopDepartment();

    Long queryDepartmentPageCount(HashMap<String, Object> map);

    List<DepartmentDto> queryDepartmentPageList(HashMap<String, Object> map);

    List<TreeNode> getAllDeptTreeNode(@Param("queryDate") String queryDate);

    int querySameDepartmentCount(DepartmentDto departmentDto);

    Long insertDepartment(DepartmentDto departmentDto);

    DepartmentDto queryDepartmentById(@Param("id") Long id, @Param("queryDate") String queryDate);

    DepartmentDto queryDepartmentByPkid(@Param("pkid") Long pkid);

    int updateDepartmentByPkid(DepartmentDto departmentDto);
}
