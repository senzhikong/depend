package com.senzhikong.sms.aliyun;

import com.alibaba.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Shu.Zhou
 */
@Getter
@Setter
public class AliyunSmsResponse {

    private static final String OK = "OK";
    /**
     * 请求Id
     */
    private String requestId;
    /**
     * 发送回执ID，可根据该ID在接口QuerySendDetails中
     */
    private String bizId;
    /**
     * 请求状态码,返回OK代表请求成功。
     */
    private String code;
    /**
     * 状态码的描述
     */
    private String message;

    public Boolean isSuccess() {
        return OK.equalsIgnoreCase(code);
    }

    /**
     *
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
