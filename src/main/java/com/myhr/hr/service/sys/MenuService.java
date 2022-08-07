package com.myhr.hr.service.sys;


import com.myhr.common.BaseResponse;
import com.myhr.hr.vo.MenuVo;

import java.util.List;

public interface MenuService {
    List<MenuVo> leftLevel1List();

    List<MenuVo> leftLevel2List();

    List<MenuVo> getAllMenuTree();

    BaseResponse getMenuPageList(MenuVo menuVo);

    BaseResponse addUpdateMenu(MenuVo menuVo);

    BaseResponse deleteMenu(MenuVo menuVo);

}
