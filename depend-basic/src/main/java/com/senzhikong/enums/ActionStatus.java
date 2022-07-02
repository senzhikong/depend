package com.senzhikong.enums;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum ActionStatus {
    /**
     * 成功
     */
    SUCCESS("success", "成功"),
    /**
     * 失败
     */
    FAIL("fail", "失败"),
    /**
     * 未知
     */
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    ActionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
