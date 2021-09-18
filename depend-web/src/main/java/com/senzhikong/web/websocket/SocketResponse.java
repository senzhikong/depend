package com.senzhikong.web.websocket;

import com.alibaba.fastjson.JSON;
import com.senzhikong.web.ajax.ApiStatus;

/**
 * @author Shu.zhou
 * @date 2018年12月4日下午2:54:12
 */
public class SocketResponse {

    private String action;

    // 返回错误编码
    private int code = ApiStatus.OK.value();
    // 返回错误信息
    private String message = ApiStatus.OK.message();
    // 返回数据,如果出错返回null
    private Object data;

    public SocketResponse(int code, String message, Object data) {
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public SocketResponse(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public static SocketResponse ok(String message) {
        return new SocketResponse(ApiStatus.OK.value(), message);
    }

    public static SocketResponse ok(Object data) {
        return new SocketResponse(ApiStatus.OK.value(), ApiStatus.OK.message(), data);
    }

    public static SocketResponse ok(String message, Object data) {
        return new SocketResponse(ApiStatus.OK.value(), message, data);
    }

    public static SocketResponse ok() {
        return new SocketResponse(ApiStatus.OK.value(), ApiStatus.OK.message());
    }

    public static SocketResponse error(String message) {
        return new SocketResponse(ApiStatus.ERROR.value(), message);
    }

    public static SocketResponse error(Object data) {
        return new SocketResponse(ApiStatus.ERROR.value(), ApiStatus.ERROR.message(), data);
    }

    public static SocketResponse error(String message, Object data) {
        return new SocketResponse(ApiStatus.ERROR.value(), message, data);
    }

    public static SocketResponse error() {
        return new SocketResponse(ApiStatus.ERROR.value(), ApiStatus.ERROR.message());
    }

    public static SocketResponse DataError(String message) {
        return new SocketResponse(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message);
    }

    public static SocketResponse DataError(Object data) {
        return new SocketResponse(ApiStatus.PARAMS_VALIDATE_ERROR.value(),
                ApiStatus.PARAMS_VALIDATE_ERROR.message(), data);
    }

    public static SocketResponse DataError(String message, Object data) {
        return new SocketResponse(ApiStatus.PARAMS_VALIDATE_ERROR.value(), message, data);
    }

    public static SocketResponse DataError() {
        return new SocketResponse(ApiStatus.PARAMS_VALIDATE_ERROR.value(),
                ApiStatus.PARAMS_VALIDATE_ERROR.message());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getAction() {
        return action;
    }


    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
