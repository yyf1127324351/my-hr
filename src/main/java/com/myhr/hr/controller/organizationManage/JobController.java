package com.myhr.hr.controller.organizationManage;

import com.myhr.hr.controller.common.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Description 岗位管理controller
 * @DBTable org_job
 */

@Slf4j
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

    /**
     * 岗位管理页面
     * */
    @RequestMapping("/goJobPage")
    public ModelAndView goJobPage(){
        return new ModelAndView("/organizationManage/jobPage");
    }







}
