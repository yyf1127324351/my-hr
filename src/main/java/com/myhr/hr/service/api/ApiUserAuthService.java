package com.myhr.hr.service.api;

import com.myhr.common.BaseResponse;

public interface ApiUserAuthService {
    BaseResponse userAuthSync();

    BaseResponse userRoleSync();
}
