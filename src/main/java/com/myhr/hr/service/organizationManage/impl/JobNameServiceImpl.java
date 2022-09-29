package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.JobNameMapper;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.model.JobNameDto;
import com.myhr.hr.service.organizationManage.JobNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Description
 * @Author yyf
 * @Date 2022-09-29 18:08
 */
@Service
public class JobNameServiceImpl implements JobNameService {
    @Autowired
    JobNameMapper jobNameMapper;

    @Override
    public BaseResponse queryJobNamePageList(HashMap<String, Object> map) {
        BaseResponse baseResponse = new BaseResponse();
        Long total = jobNameMapper.queryJobNamePageCount(map);
        baseResponse.setTotal(total);
        if (total == 0) {
            return baseResponse;
        }
        List<JobNameDto> list = jobNameMapper.queryJobNamePageList(map);
        baseResponse.setRows(list);

        return baseResponse;
    }
}
