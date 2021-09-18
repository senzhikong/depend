package com.senzhikong.sms.aliyun;

import com.alibaba.fastjson.JSON;

/**
 * @author Shu.Zhou
 */
public class AliyunSmsResponse {

    private static final String OK = "OK";
    private String requestId;// 请求Id
    private String bizId;    // 发送回执ID，可根据该ID在接口QuerySendDetails中
    private String code;     // 请求状态码,返回OK代表请求成功。
    private String message;  // 状态码的描述。

    public Boolean isSuccess() {
        return OK.equalsIgnoreCase(code);
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        this.bizId = bizId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
