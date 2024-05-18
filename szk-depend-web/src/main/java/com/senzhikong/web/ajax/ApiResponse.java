package com.senzhikong.web.ajax;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * json返回实体类
 *
 * @author Shu.zhou
 */
@Data
public class ApiResponse<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     * 返回错误编码
     */
    private int code = ApiStatus.OK.value();
    /**
     * 返回错误信息
     */
    private String message = ApiStatus.OK.message();
    /**
     * 返回数据,如果出错返回null
     */
    private T data;
    private Object extension;

    public ApiResponse() {

    }

    public ApiResponse(ApiStatus status, T data) {
        this.setCode(status.value());
        this.setMessage(status.message());
        this.setData(data);
    }

    public ApiResponse(ApiStatus status) {
        this.setCode(status.value());
        this.setMessage(status.message());
    }

    public ApiResponse(int code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public ApiResponse(int code, T data) {
        this.setCode(code);
        this.setData(data);
    }

    public ApiResponse(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public static <T> ApiResponse<T> of(ApiStatus status, T data) {
        return new ApiResponse<>(status, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ApiResponse.of(ApiStatus.OK, data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(ApiStatus.OK.value(), message, data);
    }

    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(ApiStatus.OK.value(), ApiStatus.OK.message());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(ApiStatus.ERROR.value(), message);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(ApiStatus.ERROR.value(), message, data);
    }

    public static ApiResponse<Object> error() {
        return new ApiResponse<>(ApiStatus.ERROR.value(), ApiStatus.ERROR.message());
    }

    public static ApiResponse<Object> dataError(String message) {
        return new ApiResponse<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message);
    }

    public static <T> ApiResponse<T> dataError(String message, T data) {
        return new ApiResponse<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message, data);
    }

    public static ApiResponse<Object> dataError() {
        return new ApiResponse<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(),
                ApiStatus.PARAMS_VALIDATE_ERROR.message());
    }

    public boolean isSuccess() {
        return code == ApiStatus.OK.value();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
