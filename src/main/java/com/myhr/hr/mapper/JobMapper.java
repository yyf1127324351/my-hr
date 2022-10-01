package com.myhr.hr.mapper;

import com.myhr.hr.model.JobDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface JobMapper {
    Long queryJobPageCount(HashMap<String, Object> map);

    List<JobDto> queryJobPageList(HashMap<String, Object> map);

    int querySameJobCount(JobDto jobDto);

    int updateJob(JobDto jobDto);

    JobDto queryJobById(@Param("id") Long id);

    int insertJob(JobDto jobDto);

    int insertJobWithJobId(JobDto jobDto);
}
