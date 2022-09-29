package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.JobMapper;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.model.UserDto;
import com.myhr.hr.service.organizationManage.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description 岗位管理实现类
 */
@Service
public class JobServiceImpl implements JobService {
    @Autowired
    JobMapper jobMapper;

    @Override
    public BaseResponse queryJobPageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        Long total = jobMapper.queryJobPageCount(map);
        baseResponse.setTotal(total);
        if (total == 0) {
            return baseResponse;
        }
        List<JobDto> list = jobMapper.queryJobPageList(map);
        baseResponse.setRows(list);

        return baseResponse;
    }
}
