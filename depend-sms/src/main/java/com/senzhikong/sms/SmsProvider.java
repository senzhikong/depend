package com.senzhikong.sms;

/**
 * @author Shu.Zhou
 */
public enum SmsProvider {
    ALIYUN("Aliyun", "阿里云");

    private String code;
    private String description;

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
