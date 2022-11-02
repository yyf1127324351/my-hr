package com.myhr.rabbitMq;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-13 16:17
 */
public class MessageContent<T> {

    private String id;

    private String token;

    private String operation;

    private T data;

    public MessageContent() {
        this.id = RandomStringUtils.randomAlphabetic(32);
    }

    public MessageContent(String operation,String token, T data) {
        this.id = RandomStringUtils.randomAlphabetic(32);
        this.operation = operation;
        this.token = token;
        this.data = data;
    }

    public static <T> MessageContent<T> build(String operateType,String token, T data) {
        return new MessageContent<>(operateType,token, data);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
