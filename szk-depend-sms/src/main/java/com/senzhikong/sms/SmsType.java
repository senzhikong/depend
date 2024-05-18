package com.senzhikong.sms;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum SmsType {
    /**
     * 验证码
     */
    VERIFY_CODE("verify_code", "验证码");

    private final String code;
    private final String description;

    SmsType(String code, String description) {
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
