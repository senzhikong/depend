package com.senzhikong.db.enums;

/**
 * 审核状态
 *
 * @author Shu.Zhou
 */
public enum AuthStatus {
    AUDIT("audit", "审核中"),
    PASS("pass", "通过"),
    REFUSE("refuse", "驳回"),
    UNKNOW("unknow", "未知");

    private String code;
    private String description;

    AuthStatus(String code, String description) {
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
