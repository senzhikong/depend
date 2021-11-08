package com.senzhikong.enums;

import lombok.Getter;

/**
 * 审核状态
 *
 * @author Shu.Zhou
 */
@Getter
public enum AuthStatus {
    AUDIT("audit", "审核中"),
    PASS("pass", "通过"),
    REFUSE("refuse", "驳回"),
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

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
}
