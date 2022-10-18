package com.myhr.rabbitMq;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-13 16:17
 */
public class MessageContent<T> {

    private String id;

    private String operateType;

    private T data;

    public MessageContent() {
        this.id = RandomStringUtils.randomAlphabetic(32);
    }

    public MessageContent(String operateType, T data) {
        this.id = RandomStringUtils.randomAlphabetic(32);
        this.operateType = operateType;
        this.data = data;
    }

    public static <T> MessageContent<T> build(String operateType, T data) {
        return new MessageContent<>(operateType, data);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
