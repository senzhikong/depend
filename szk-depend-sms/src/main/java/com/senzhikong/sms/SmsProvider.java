package com.senzhikong.sms;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum SmsProvider {
    /**
     * 阿里云
     */
    ALIYUN("Aliyun", "阿里云");

    private final String code;
    private final String description;

    SmsProvider(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }
}
