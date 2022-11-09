package com.myhr.auth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description Auth系统用户角色对应关系
 */

public class UserAuth {
    private String userNo; //用户登录名
    private Map<String, List<String>> auths;

    public UserAuth() {
        this.auths = new HashMap<>();
    }

    public String getUserNo() {
        return userNo;
    }

    public UserAuth setUserNo(String userNo) {
        this.userNo = userNo;
        return this;
    }

    public Map<String, List<String>> getAuths() {
        return auths;
    }

    public void setAuths(Map<String, List<String>> auths) {
        this.auths = auths;
    }
}
