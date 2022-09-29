package com.myhr.hr.service.organizationManage;

import com.myhr.common.BaseResponse;

import java.util.HashMap;

public interface JobService {
    BaseResponse queryJobPageList(HashMap<String, Object> map);
}
