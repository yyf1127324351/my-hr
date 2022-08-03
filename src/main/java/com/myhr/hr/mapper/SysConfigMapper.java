package com.myhr.hr.mapper;

import com.myhr.hr.model.SysConfigTypeDto;
import com.myhr.hr.model.SysConfigValueDto;
import com.myhr.hr.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
@Mapper
@Repository
public interface SysConfigMapper {
    List<SysConfigTypeDto> getAllConfigType();

    Long getSysConfigPageCount(HashMap<String, Object> map);

    List<SysConfigValueDto> getSysConfigPageList(HashMap<String, Object> map);

    void updateSysConfigType(SysConfigTypeDto paramModel);

    void addSysConfigType(SysConfigTypeDto sysConfigTypeDto);

    void addSysConfigValue(SysConfigValueDto sysConfigValueDto);

    void deleteSysConfigType(@Param("typeId") Long typeId);

    void deleteSysConfigValueByTypeId(@Param("typeId") Long typeId);

    void updateSysConfigValue(SysConfigValueDto sysConfigValueDto);

    List<TreeNode> getAllAreaTreeNode();
}
