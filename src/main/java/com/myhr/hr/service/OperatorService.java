package com.myhr.hr.service;

import com.myhr.hr.model.Operator;

import java.util.Map;

public interface OperatorService {
    Operator getOperatorByMap(Map<String, String> loginName);
}
