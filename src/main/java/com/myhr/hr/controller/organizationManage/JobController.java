package com.myhr.hr.controller.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.common.SessionContainer;
import com.myhr.hr.controller.common.BaseController;
import com.myhr.hr.model.JobDto;
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

    /**
     * 岗位管理-新增/更新
     * */
    @RequestMapping("/saveOrUpdateJob")
    @ResponseBody
    public BaseResponse saveOrUpdateJob(JobDto jobDto){
        try {
            Long updateUser = SessionContainer.getUserId();
            if (null != jobDto.getId()) {
                //更新
                return BaseResponse.success();
            }else {
                //新增
                return jobService.addJob(jobDto,updateUser);
            }
        }catch (Exception e){
            log.error("saveOrUpdateJobException:" + e.getMessage(), e);
            return BaseResponse.error();
        }

    }



}
