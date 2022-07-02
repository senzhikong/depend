package com.senzhikong.enums;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum CommonStatus {
    /**
     * 正常
     */
    NORMAL("normal", "正常"),
    /**
     * 禁用
     */
    FORBIDDEN("forbidden", "禁用"),
    /**
     * 已删除
     */
    DELETE("delete", "已删除"),
    /**
     * 未知
     */
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

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

    public String getDescription() {
        return description;
    }

}
