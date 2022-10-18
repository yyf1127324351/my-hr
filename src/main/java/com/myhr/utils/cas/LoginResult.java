package com.myhr.utils.cas;

import lombok.Data;

/**
 * 登陆结果
 */
@Data
public class LoginResult {

    /**
     * 是否成功
     */
    private boolean success;

    /**
     * TGT
     */
    private String tgt;

    /**
     * ST
     */
    private String st;

    /**
     * 组织编码
     */
    private String organizationCode;

}
