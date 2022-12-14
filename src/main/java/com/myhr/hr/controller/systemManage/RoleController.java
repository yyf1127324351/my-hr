package com.myhr.hr.controller.systemManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.model.AuthRoleFieldDto;
import com.myhr.hr.model.RoleAuthorityDto;
import com.myhr.hr.model.RoleDto;
import com.myhr.hr.service.systemManage.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/role")
@Slf4j
public class RoleController extends BaseController {

    @Autowired
    RoleService roleService;

    /**
     * 转跳系统参数配置页面
     */
    @RequestMapping("/goRolePage")
    public ModelAndView goRolePage() {
        return new ModelAndView("/systemManage/rolePage");
    }

    /**
     * 获取角色列表
     */
    @RequestMapping(value = "/getRolePageList", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse getRolePageList(HttpServletRequest request) {
        try {
            HashMap<String, Object> map = getParametersMap(request);
            return roleService.getRolePageList(map);
        } catch (Exception e) {
            log.error("getRolePageList:{}", e);
            return BaseResponse.error();
        }

    }

    /**
     * 添加角色
     */
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse addRole(RoleDto roleDto) {
        try {
            roleService.addRole(roleDto);
            return BaseResponse.success();
        } catch (Exception e) {
            log.error("addRole:{}", e);
            return BaseResponse.error();
        }

    }

    /**
     * 更新角色
     */
    @RequestMapping(value = "/updateRole", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse updateRole(RoleDto roleDto) {
        try {
            roleService.updateRole(roleDto);
            return BaseResponse.success();
        } catch (Exception e) {
            log.error("updateRoleStatus:{}", e);
            return BaseResponse.error();
        }

    }

    /**
     * 角色管理-分配权限（获取角色对应的权限）
     */
    @RequestMapping(value = "/getAuthTree", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse getAuthTree(Long roleId) {
        try {
            Map<String, List> treeMap = roleService.getAuthTree(roleId);
            return BaseResponse.success(treeMap);
        } catch (Exception e) {
            log.error("getAuthTree:{}", e);
            e.printStackTrace();
            return BaseResponse.error();
        }

    }

    /**
     * 角色管理-分配权限（更新角色对应的权限）
     */
    @RequestMapping(value = "/saveAuthTree", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse saveAuthTree(RoleAuthorityDto roleAuthorityDto) {
        try {
            roleService.saveAuthTree(roleAuthorityDto);
            return BaseResponse.success("权限保存成功");
        } catch (Exception e) {
            log.error("saveAuthTree:{}", e);
            e.printStackTrace();
            return BaseResponse.error();
        }

    }


    /**
     * 角色管理-分配列属性权限（获取角色对应的列属性权限）
     */
    @RequestMapping("/getRoleField")
    @ResponseBody
    private BaseResponse getRoleField(@RequestParam("roleId") Long roleId){
        try {
            return roleService.getRoleField(roleId);
        } catch (Exception e) {
            log.error("getRoleFieldException:" + e.getMessage(), e);
            return BaseResponse.error();
        }
    }

    /**
     * 角色管理-分配列属性权限（更新角色对应的列属性权限）
     */
    @RequestMapping("/updateAuthRoleField")
    @ResponseBody
    private BaseResponse updateAuthRoleField(AuthRoleFieldDto authRoleFieldDto){

        try {
            Long updateUser = SessionContainer.getUserId();
            return roleService.updateAuthRoleField(authRoleFieldDto,updateUser);
        } catch (Exception e) {
            log.error("updateAuthRoleFieldException:" + e.getMessage(), e);
            return BaseResponse.error("updateAuthRoleFieldError");
        }
    }



    /**
     * 获取所有角色
     */
    @RequestMapping(value = "/getRoleList")
    @ResponseBody
    public List<RoleDto> getRoleList() {
        try {
            return roleService.getRoleList();
        } catch (Exception e) {
            log.error("getRoleListException" + e.getMessage(), e);
            return Collections.emptyList();
        }

    }

}
