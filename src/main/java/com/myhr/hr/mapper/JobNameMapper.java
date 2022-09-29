package com.myhr.hr.mapper;


import com.myhr.hr.model.JobNameDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Mapper
@Repository
public interface JobNameMapper {

    Long queryJobNamePageCount(HashMap<String, Object> map);

    List<JobNameDto> queryJobNamePageList(HashMap<String, Object> map);
}
