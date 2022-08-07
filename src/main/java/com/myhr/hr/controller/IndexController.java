package com.myhr.hr.controller;

import com.myhr.common.SessionContainer;
import com.myhr.hr.service.sys.MenuService;
import com.myhr.hr.vo.MenuVo;
import org.apache.commons.collections.CollectionUtils;
import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @Author yyf
 * @Date 2022-06-15 23:15
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private MenuService menuService;

    //首页
    @GetMapping("")
    public ModelAndView goIndex(HttpServletRequest request){

        ModelAndView mv=new ModelAndView("index");//模板文件的名称，不需要指定后缀
        mv.addObject("userName", SessionContainer.getUserName());
        mv.addObject("userId", SessionContainer.getUserName());
        //根据登陆人id获取其有权限的菜单列表
//        List<MenuVo> menuList = menuService. getUserMenuList(SessionContainer.getUserId());

        // 顶级菜单权限控制
        List<MenuVo> level1List = menuService.leftLevel1List();
        List<MenuVo> level2List = menuService.leftLevel2List();
        if (CollectionUtils.isNotEmpty(level2List)) {
            for (MenuVo menuVo1 : level1List) {
                List<MenuVo> children = new ArrayList<>();
                for (MenuVo p2 : level2List) {
                    if (menuVo1.getId().toString().equals(p2.getParentId().toString())) {
                        children.add(p2);
                    }
                }
                menuVo1.setChildren(children);
            }
        }

        mv.addObject("level1List", level1List);


        return mv;
    }

}
