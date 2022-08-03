package com.myhr.hr.service.sys.impl;
import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.mapper.MenuMapper;
import com.myhr.hr.model.MenuDto;
import com.myhr.hr.service.sys.MenuService;
import com.myhr.hr.vo.MenuVo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * 系统菜单service实现类
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuMapper menuMapper;


    @Override
    public List<MenuVo> leftLevel1List() {
        //查出一级菜单
        List<MenuVo> left = menuMapper.leftLevel1List(SessionContainer.getUserId());
        // 转化成easyui tree需要的树形数据
        List<MenuVo> treeList = MenuVo.convertToTreeDataByParentId(left);
        this.sortMenuList(treeList);
        return treeList;
    }

    @Override
    public List<MenuVo> leftLevel2List() {
        //查出二级菜单
        List<MenuVo> left = menuMapper.leftLevel2List(SessionContainer.getUserId());
        // 转化成easyui tree需要的树形数据
        List<MenuVo> treeList = MenuVo.convertToTreeDataByParentId(left);
        this.sortMenuList(treeList);
        return treeList;
    }

    @Override
    public List<MenuVo> getAllMenuTree() {
        List<MenuVo> list = new ArrayList<>();
        //查询出所有的菜单
        List<MenuVo> menuVoList = menuMapper.getAllMenuTree();
        if (CollectionUtils.isNotEmpty(menuVoList)) {
            List<MenuVo> newMenuVoList = MenuVo.convertToTreeDataByParentId(menuVoList);
            MenuVo menuVo = new MenuVo();
            menuVo.setId(0L);
            menuVo.setState("close");
            menuVo.setText("菜单");
            menuVo.setName("菜单");
            menuVo.setHasChild(1);
            menuVo.setLevel(0);
            menuVo.setChildren(newMenuVoList);
            list.add(menuVo);
        }
        return list;



//        List<TreeNode> nodeList = new ArrayList<>();
//        menuList.forEach(e -> {
//            TreeNode node = new TreeNode();
//            node.setId(e.getId());
//            node.setText(e.getName());
//            node.setParentId(e.getParentId());
//            node.setLevel(node.getLevel());
//            node.setHasChild(e.getHasChild());
//            nodeList.add(node);
//        });
//        //转成所有1级菜单树
//        List<TreeNode> treeNodeList = TreeNode.convertToTreeList(nodeList);
//
//        TreeNode rootTreeNode = new TreeNode();
//        rootTreeNode.setId(0);
//        rootTreeNode.setState("close");
//        rootTreeNode.setText("菜单");
//        rootTreeNode.setHasChild(1);
//        rootTreeNode.setChildren(treeNodeList);
//
//        List<TreeNode> list = new ArrayList<>();
//        list.add(rootTreeNode);


    }

    @Override
    public BaseResponse getMenuPageList(MenuVo menuVo) {
        BaseResponse baseResponse = new BaseResponse();
        if (StringUtils.isNotBlank(menuVo.getMenuIds())) {
            List<String> ids = Arrays.asList(menuVo.getMenuIds().split(","));
            menuVo.setIds(ids);
        }
        baseResponse.setTotal(menuMapper.getMenuPageCount(menuVo));
        List<MenuVo> list = menuMapper.getMenuPageList(menuVo);
        baseResponse.setRows(list);
        return baseResponse;
    }

    @Override
    public BaseResponse addUpdateMenu(MenuVo menuVo) {
        if (null == menuVo.getId()) {
            //新增菜单
            menuMapper.insert(menuVo);
            //更新父菜单 是否有子菜单字段
            menuVo.setHasChild(1);
            menuMapper.updateParentMenuHasChildren(menuVo);

        }else {
            //更新菜单
            menuMapper.update(menuVo);
        }

        return BaseResponse.success();
    }

    @Override
    public BaseResponse deleteMenu(MenuVo menuVo) {
        if (StringUtils.isNotBlank(menuVo.getMenuIds())) {
            List<String> ids = Arrays.asList(menuVo.getMenuIds().split(","));
            menuVo.setIds(ids);
            //逻辑删除菜单
            menuMapper.deleteMenu(menuVo);
            //如果该菜单的父菜单下无菜单了，就将父菜单 是否有子菜单更新为： 无
            List<MenuDto> menuDtoList = menuMapper.getMenuByParentId(menuVo.getParentId());
            if (CollectionUtils.isEmpty(menuDtoList)) {
                menuVo.setHasChild(0);
                menuMapper.updateParentMenuHasChildren(menuVo);
            }
            return BaseResponse.success();
        }else {
            return BaseResponse.paramError("菜单参数为空");
        }

    }

    // 排序
    private void sortMenuList(List<MenuVo> list) {
        list.sort(comparing(MenuVo::getSortNumber));
        for (MenuVo menuVo : list) {
            if (CollectionUtils.isNotEmpty(menuVo.getChildren())) {
                this.sortMenuList(menuVo.getChildren());
            }
        }
    }

}
