package com.senzhikong.enums;

import lombok.Getter;

/**
 * 审核状态
 *
 * @author Shu.Zhou
 */
@Getter
public enum AuthStatus {
    /**
     * 审核中
     */
    AUDIT("audit", "审核中"),
    /**
     * 通过
     */
    PASS("pass", "通过"),
    /**
     * 驳回
     */
    REFUSE("refuse", "驳回"),
    /**
     * 未知
     */
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    AuthStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
