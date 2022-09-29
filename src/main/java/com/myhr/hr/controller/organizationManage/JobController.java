package com.myhr.hr.controller.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.service.organizationManage.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @Description 岗位管理controller
 * @DBTable org_job
 */

@Slf4j
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

    @Autowired
    JobService jobService;

    /**
     * 岗位管理页面
     * */
    @RequestMapping("/goJobPage")
    public ModelAndView goJobPage(){
        return new ModelAndView("/organizationManage/jobPage");
    }


    /**
     * 岗位管理列表
     * */
    @RequestMapping("/queryJobPageList")
    @ResponseBody
    public BaseResponse queryJobPageList(HttpServletRequest request){
        try {
            HashMap<String, Object> map = getParametersMap(request);
            return jobService.queryJobPageList(map);
        }catch (Exception e){
            log.error("queryJobPageListException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }



}
