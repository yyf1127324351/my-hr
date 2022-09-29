package com.myhr.hr.controller.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.service.organizationManage.JobNameService;
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
 * @Description 岗位名称管理controller
 * @DBTable org_job
 */

@Slf4j
@Controller
@RequestMapping("/jobName")
public class JobNameController extends BaseController {

    @Autowired
    JobNameService jobNameService;

    /**
     * 岗位管理页面
     * */
    @RequestMapping("/goJobNamePage")
    public ModelAndView goJobPage(){
        return new ModelAndView("/organizationManage/jobNamePage");
    }


    /**
     * 岗位管理列表
     * */
    @RequestMapping("/queryJobNamePageList")
    @ResponseBody
    public BaseResponse queryJobNamePageList(HttpServletRequest request){
        try {
            HashMap<String, Object> map = getParametersMap(request);
            return jobNameService.queryJobNamePageList(map);
        }catch (Exception e){
            log.error("queryJobNamePageListException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }

}
