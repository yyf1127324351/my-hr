package com.myhr.hr.service.impl;

import com.myhr.hr.mapper.OperatorMapper;
import com.myhr.hr.model.Operator;
import com.myhr.hr.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OperatorServiceImpl implements OperatorService {

    @Autowired
    OperatorMapper operatorMapper;

    @Override
    public Operator getOperatorByMap(Map<String, String> map) {
        return operatorMapper.getOperatorByMap(map);
    }
}
