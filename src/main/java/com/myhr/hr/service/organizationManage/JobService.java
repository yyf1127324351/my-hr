package com.myhr.hr.service.organizationManage;

import com.myhr.common.BaseResponse;
import com.myhr.hr.model.JobDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public interface JobService {
    BaseResponse queryJobPageList(HashMap<String, Object> map);

    BaseResponse addJob(JobDto jobDto, Long updateUser);

    BaseResponse updateJob(JobDto jobDto, Long updateUser);

    BaseResponse changeJob(JobDto jobDto, Long updateUser);

    void exportJob(HashMap<String, Object> map, String fileName, HttpServletRequest request, HttpServletResponse response);
}
