package com.senzhikong.sms;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum SmsType {
    VERIFY_CODE("verify_code", "未发送");

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
