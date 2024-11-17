package com.senzhikong.basic.dto;

import com.alibaba.fastjson.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * json返回实体类
 *
 * @author Shu.zhou
 */
@Data
public class ApiResp<T> implements Serializable {
    public static final Integer OK = 0;
    public static final String OK_MESSAGE = "请求成功";
    public static final Integer ERROR = -1;
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "返回错误编码")
    private int code = OK;
    @Schema(description = "返回错误信息")
    private String message = OK_MESSAGE;
    @Schema(description = "返回数据,如果出错返回null")
    private T data;

    public ApiResp() {
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

    public boolean success() {
        return OK == code;
    }

    public static <T> ApiResp<T> ok() {
        return new ApiResp<>(OK, OK_MESSAGE);
    }

    public static <T> ApiResp<T> ok(T data) {
        return new ApiResp<>(OK, OK_MESSAGE, data);
    }

    public static <T> ApiResp<T> ok(String message, T data) {
        return new ApiResp<>(OK, message, data);
    }

    public static <T> ApiResp<T> error(String message) {
        return new ApiResp<>(ERROR, message);
    }

    public static <T> ApiResp<T> error(String message, T data) {
        return new ApiResp<>(ERROR, message, data);
    }


    public static <K> ApiResp<PagerResp<K>> okPage(PagerResp<?> pagerResp, List<K> dataList) {
        return new ApiResp<>(OK, OK_MESSAGE, new PagerResp<>(pagerResp, dataList));
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
