package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.JobMapper;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.service.organizationManage.JobService;
import com.myhr.utils.DateUtil;
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
        for (JobDto jobDto : list) {
            String nowDay = DateUtil.getTodayDate();
            if (DateUtil.isBefore(jobDto.getEndDate(), nowDay)) {
                jobDto.setIsValid(0);
            }
        }
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
        jobMapper.insertJob(jobDto);

        return BaseResponse.success("新增岗位成功");
    }

    @Override
    public BaseResponse updateJob(JobDto jobDto, Long updateUser) {
        //校验该岗位是否存在
        BaseResponse checkResult = this.checkJobIsExit(jobDto);
        if (checkResult.getCode() != 200) {
            return checkResult;
        }
        jobDto.setUpdateUser(updateUser);
        jobDto.setUpdateTime(new Date());
        jobMapper.updateJob(jobDto);
        return BaseResponse.success("更新岗位成功");

    }

    @Override
    public BaseResponse changeJob(JobDto jobDto, Long updateUser) {
        if (!DateUtil.isAfter(jobDto.getEndDate(), jobDto.getStartDate())) {
            //如果 新失效日期 <= 新生效日期
            return BaseResponse.paramError("失效日期必须晚于生效日期");
        }

        //将原来的岗位失效日期，生效日期进行更新，再新增一个新的岗位
        //获取旧岗位
        JobDto oldJob = jobMapper.queryJobById(jobDto.getId());
        if (!DateUtil.isAfter(jobDto.getStartDate(), oldJob.getStartDate())
                || !DateUtil.isBefore(jobDto.getStartDate(),oldJob.getEndDate())) {
            //如果 新生效日期 <= 原生效日期 或者 新生效日期 > 原失效日期
            return BaseResponse.paramError("新生效日期必须晚于原生效日期，且新生效日期不能晚于原失效日期");
        }else {
            //获取 新生效日期的前一天
            String oldEndDate = DateUtil.subDayDate(jobDto.getStartDate(), 1);
            oldJob.setEndDate(oldEndDate);
            oldJob.setUpdateUser(updateUser);
            oldJob.setUpdateTime(new Date());
            jobMapper.updateJob(oldJob);

            //插入最新的 岗位
            jobDto.setJobId(oldJob.getJobId());
            jobDto.setJobName(oldJob.getJobName());
            jobDto.setJobNameId(oldJob.getJobNameId());
            jobDto.setCreateTime(new Date());
            jobDto.setCreateUser(updateUser);
            jobMapper.insertJobWithJobId(jobDto);
        }
        return BaseResponse.success("变更成功");
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

        if (DateUtil.isBefore(jobDto.getEndDate(), jobDto.getStartDate())) {
            return BaseResponse.paramError("失效日期不能早于生效日期");
        }


        //校验该岗位数据库中是否存在
        int count = jobMapper.querySameJobCount(jobDto);
        if (count > 0) {
            return BaseResponse.paramError("该岗位已经存在");
        }

        return BaseResponse.success();
    }
}
