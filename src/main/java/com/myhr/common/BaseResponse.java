package com.myhr.common;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class BaseResponse<T> {


    private Integer code;

    private String message;

    // 自定义返回数据
    // @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    // easyui datagrid 需要这2个数据才能正确运行事件
    private Long total = 0L;
    private List rows = new ArrayList<>();

    public BaseResponse() {
        this.code = ResponseCode.SUCCESS.value;
        this.message = ResponseCode.SUCCESS.description;
    }

    public BaseResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    private BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(ResponseCode.SUCCESS.value, ResponseCode.SUCCESS.description, null);
    }

    public static <T> BaseResponse<T> success(String message) {
        return new BaseResponse<>(ResponseCode.SUCCESS.value, message, null);
    }
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS.value, ResponseCode.SUCCESS.description, data);
    }
    public static <T> BaseResponse<T> success(String message, T data) {
        return new BaseResponse<>(ResponseCode.SUCCESS.value, message, data);
    }
    public static <T> BaseResponse<T> error() {
        return new BaseResponse<>(ResponseCode.SYS_ERROR.value, ResponseCode.SYS_ERROR.description, null);
    }
    public static <T> BaseResponse<T> error(String message) {
        return new BaseResponse<>(ResponseCode.SYS_ERROR.value, message, null);
    }
    public static <T> BaseResponse<T> paramError() {
        return new BaseResponse<>(ResponseCode.PARAM_CHECK_FAIL.value, ResponseCode.PARAM_CHECK_FAIL.description, null);
    }
    public static <T> BaseResponse<T> paramError(String message) {
        return new BaseResponse<>(ResponseCode.PARAM_CHECK_FAIL.value, message, null);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List getRows() {
        return rows;
    }

    public void setRows(List rows) {
        this.rows = rows;
    }

    public enum ResponseCode{

        SUCCESS(200,"success"),

        PARAM_CHECK_FAIL(442,"request params check fail"),

        SYS_ERROR(500,"system error");


        private Integer value;
        private String description;

        ResponseCode(Integer value, String description) {
            this.value = value;
            this.description = description;
        }

        public Integer getValue() {
            return value;
        }


    }


}
