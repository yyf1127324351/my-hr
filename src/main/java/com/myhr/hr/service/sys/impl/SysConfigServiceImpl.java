package com.myhr.hr.service.sys.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.SysConfigMapper;
import com.myhr.hr.model.SysConfigTypeDto;
import com.myhr.hr.model.SysConfigValueDto;
import com.myhr.hr.service.sys.SysConfigService;
import com.myhr.hr.vo.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    SysConfigMapper sysConfigMapper;

    @Override
    public List<TreeNode> getSysConfigTree() {
        List<SysConfigTypeDto> typeDtos = sysConfigMapper.getAllConfigType();
        List<TreeNode> nodeList = new ArrayList<>();
        typeDtos.forEach(e ->{
            TreeNode node = new TreeNode();
//            node.setIconCls("icon-tip");
            node.setId(e.getId());
            node.setText(e.getName());
            node.setCode(e.getTypeCode());
            node.setSortNumber(e.getSortNumber());
            nodeList.add(node);
        });

        TreeNode rootTreeNode = new TreeNode();
        rootTreeNode.setId(0L);
        rootTreeNode.setText("系统参数类型");
        rootTreeNode.setState("close");
        rootTreeNode.setHasChild(1);
        rootTreeNode.setChildren(nodeList);

        return Collections.singletonList(rootTreeNode);
    }

    @Override
    public BaseResponse getSysConfigPageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setTotal(sysConfigMapper.getSysConfigPageCount(map));
        List<SysConfigValueDto> list = sysConfigMapper.getSysConfigPageList(map);
        baseResponse.setRows(list);
        return baseResponse;
    }

    @Override
    public void updateSysConfigType(SysConfigTypeDto sysConfigTypeDto) {
        sysConfigMapper.updateSysConfigType(sysConfigTypeDto);
    }

    @Override
    public void addSysConfigType(SysConfigTypeDto sysConfigTypeDto) {
        sysConfigMapper.addSysConfigType(sysConfigTypeDto);
    }

    @Override
    public void addSysConfigValue(SysConfigValueDto sysConfigValueDto) {
        sysConfigMapper.addSysConfigValue(sysConfigValueDto);
    }

    @Override
    public void deleteSysConfigType(Long typeId) {
        sysConfigMapper.deleteSysConfigType(typeId);
        sysConfigMapper.deleteSysConfigValueByTypeId(typeId);
    }

    @Override
    public void updateSysConfigValue(SysConfigValueDto sysConfigValueDto) {
        sysConfigMapper.updateSysConfigValue(sysConfigValueDto);
    }
}
