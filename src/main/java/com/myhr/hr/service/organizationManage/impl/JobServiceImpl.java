package com.myhr.hr.service.organizationManage.impl;

import com.myhr.common.BaseResponse;
import com.myhr.hr.mapper.JobMapper;
import com.myhr.hr.model.JobDto;
import com.myhr.hr.service.organizationManage.JobService;
import com.myhr.utils.DateUtil;
import com.myhr.utils.ExportUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
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
            baseResponse.setRows(Collections.EMPTY_LIST);
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

    @Override
    public void exportJob(HashMap<String, Object> map, String fileName, HttpServletRequest request, HttpServletResponse response) {
        map.put("page", null);
        map.put("rows", null);
        List<JobDto> list = queryJobPageList(map).getRows();
        String title = "岗位ID,岗位名称,编制人数,生效日期,失效日期,岗位名称ID";
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        for (int i = 0; i < 100; i++) {
            sheet.setColumnWidth(i, 5000);
        }
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 9);
        font.setFontName("宋体");
        font.setBoldweight((short) 700);

        XSSFRow row = sheet.createRow(0);

        ExportUtils.buildCell(row, title, workbook);
        int index = 1;
        if (CollectionUtils.isNotEmpty(list)) {
            CellStyle cellStyle = ExportUtils.centerStyle(workbook, false);
            for (JobDto model : list) {
                int cellNum = 0;
                XSSFRow row1 = sheet.createRow(index);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getJobId().toString(), cellStyle);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getJobName(), cellStyle);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getHeadcount().toString(), cellStyle);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getStartDate(), cellStyle);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getEndDate(), cellStyle);
                cellNum = ExportUtils.buildCellNew(row1, cellNum, model.getJobNameId().toString(), cellStyle);

                index++;
            }
        }
//        ExportUtils.exportExcelFile(workbook, fileName,request,response);
        ExportUtils.exportExcel(workbook, fileName,request,response);
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
