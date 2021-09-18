package com.senzhikong.db.enums;

/**
 * @author Shu.Zhou
 */
public enum CommonStatus {
    NORMAL("normal", "正常"),
    FORBIDDEN("forbidden", "禁用"),
    DELETE("delete", "已删除"),
    UNKNOW("unknow", "未知");

    private String code;
    private String description;

    CommonStatus(String code, String description) {
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
