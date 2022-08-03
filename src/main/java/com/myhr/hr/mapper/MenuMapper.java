package com.myhr.hr.mapper;

import com.myhr.hr.model.MenuDto;
import com.myhr.hr.vo.MenuVo;
import com.myhr.hr.vo.TreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MenuMapper {

    List<MenuVo> leftLevel1List(@Param("userId") Long userId);

    List<MenuVo> leftLevel2List(@Param("userId") Long userId);

    List<MenuVo> getAllMenuTree();

    Long getMenuPageCount(MenuVo menuVo);

    List<MenuVo> getMenuPageList(MenuVo menuVo);

    void insert(MenuVo menuVo);

    void updateParentMenuHasChildren(MenuVo menuVo);

    void update(MenuVo menuVo);

    List<MenuDto> getMenuByParentId(@Param("parentId") Long parentId);

    void deleteMenu(MenuVo menuVo);

    List<TreeNode> getAllMenuTreeNode();
}
