package com.myhr.hr.controller.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.service.organizationManage.DepartmentService;
import com.myhr.hr.vo.TreeNode;
import com.myhr.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * @Description 部门管理controller
 * @DBTable org_department
 */
@Slf4j
@Controller
@RequestMapping("/department")
public class DepartmentController extends BaseController {

    @Autowired
    DepartmentService departmentService;

    /**
     * 跳转部门管理页面
     * */
    @RequestMapping("/goDepartmentPage")
    public ModelAndView goDepartmentPage(){
        ModelAndView mv = new ModelAndView("/organizationManage/departmentPage");
        String queryDate = DateUtil.getTodayDate();
        mv.addObject("queryDate", queryDate);
        return mv;
    }

    /**
     * 部门管理左侧部门树
     * */
    @RequestMapping("/loadDepartmentTree")
    @ResponseBody
    public List<TreeNode> loadDepartmentTree(String queryDate) {
        Long operateUser = SessionContainer.getUserId();
        List<TreeNode> treeList = departmentService.getDepartmentTree(queryDate,operateUser);
        return treeList;
    }

    /**
     * 部门管理列表
     * */
    @RequestMapping("/queryDepartmentPageList")
    @ResponseBody
    public BaseResponse queryDepartmentPageList(HttpServletRequest request){
        try {
            HashMap<String, Object> map = getParametersMap(request);
            Long operateUser = SessionContainer.getUserId();
            map.put("operateUser", operateUser);
            return departmentService.queryDepartmentPageList(map);
        }catch (Exception e){
            log.error("queryDepartmentPageListException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }


}
