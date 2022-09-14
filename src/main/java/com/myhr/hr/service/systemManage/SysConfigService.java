package com.myhr.hr.service.systemManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.SysConfigTypeDto;
import com.myhr.hr.model.SysConfigValueDto;
import com.myhr.hr.vo.TreeNode;

import java.util.HashMap;
import java.util.List;

public interface SysConfigService {
    List<TreeNode> getSysConfigTree();

    BaseResponse getSysConfigPageList(HashMap<String, Object> map);

    void updateSysConfigType(SysConfigTypeDto sysConfigTypeDto);

    void addSysConfigType(SysConfigTypeDto sysConfigTypeDto);

    void addSysConfigValue(SysConfigValueDto sysConfigValueDto);

    void deleteSysConfigType(Long typeId);

    void updateSysConfigValue(SysConfigValueDto sysConfigValueDto);
}
