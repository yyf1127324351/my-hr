package com.myhr.hr.mapper;

import com.myhr.hr.model.JobDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface JobMapper {
    Long queryJobPageCount(HashMap<String, Object> map);

    List<JobDto> queryJobPageList(HashMap<String, Object> map);
}
