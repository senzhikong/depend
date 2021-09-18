package com.senzhikong.sms;

/**
 * @author Shu.Zhou
 */
public enum SmsType {
    VERIFY_CODE("verify_code", "未发送");

    private String code;
    private String description;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
