package com.senzhikong.web.ajax;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * json返回实体类
 *
 * @author Shu.zhou
 */
@Data
public class ApiResp<T> implements Serializable {

    /**
     *
     */
    @Serial
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

    public ApiResp() {

    }

    public ApiResp(ApiStatus status, T data) {
        this.setCode(status.value());
        this.setMessage(status.message());
        this.setData(data);
    }

    public ApiResp(ApiStatus status) {
        this.setCode(status.value());
        this.setMessage(status.message());
    }

    public ApiResp(int code, String message, T data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public ApiResp(int code, T data) {
        this.setCode(code);
        this.setData(data);
    }

    public ApiResp(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public static <T> ApiResp<T> of(ApiStatus status, T data) {
        return new ApiResp<>(status, data);
    }

    public static <T> ApiResp<T> ok(T data) {
        return ApiResp.of(ApiStatus.OK, data);
    }

    public static <T> ApiResp<T> ok(String message, T data) {
        return new ApiResp<>(ApiStatus.OK.value(), message, data);
    }

    public static <T> ApiResp<T> ok() {
        return new ApiResp<>(ApiStatus.OK.value(), ApiStatus.OK.message());
    }

    public static <T> ApiResp<T> error(String message) {
        return new ApiResp<>(ApiStatus.ERROR.value(), message);
    }

    public static <T> ApiResp<T> error(String message, T data) {
        return new ApiResp<>(ApiStatus.ERROR.value(), message, data);
    }

    public static ApiResp<Object> error() {
        return new ApiResp<>(ApiStatus.ERROR.value(), ApiStatus.ERROR.message());
    }

    public static ApiResp<Object> dataError(String message) {
        return new ApiResp<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message);
    }

    public static <T> ApiResp<T> dataError(String message, T data) {
        return new ApiResp<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message, data);
    }

    public static ApiResp<Object> dataError() {
        return new ApiResp<>(ApiStatus.PARAMS_VALIDATE_ERROR.value(),
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
