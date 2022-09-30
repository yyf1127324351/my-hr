package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.JobMapper;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.service.organizationManage.JobService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

    @Override
    public BaseResponse addJob(JobDto jobDto, Long updateUser) {
        //校验该岗位是否存在
        BaseResponse checkResult = this.checkJobIsExit(jobDto);
        if (checkResult.getCode() != 200) {
            return checkResult;
        }

        jobDto.setCreateUser(updateUser);
        jobDto.setCreateTime(new Date());
        jobMapper.addJob(jobDto);

        return BaseResponse.success("新增岗位成功");
    }

    private BaseResponse checkJobIsExit(JobDto jobDto) {
        if (null == jobDto.getJobNameId() || StringUtils.isBlank(jobDto.getJobName())) {
            return BaseResponse.paramError("岗位名称不能为空");
        }
        if (StringUtils.isBlank(jobDto.getStartDate())) {
            return BaseResponse.paramError("生效日期不能为空");
        }
        if (StringUtils.isBlank(jobDto.getEndDate())) {
            return BaseResponse.paramError("失效日期不能为空");
        }

        //校验该岗位数据库中是否存在
        int count = jobMapper.querySameJobCount(jobDto);
        if (count > 0) {
            return BaseResponse.paramError("该岗位已经存在");
        }

        return BaseResponse.success();
    }
}
