package com.myhr.hr.service.organizationManage;

import com.myhr.common.BaseResponse;

import java.util.HashMap;

public interface JobNameService {
    BaseResponse queryJobNamePageList(HashMap<String, Object> map);
}
