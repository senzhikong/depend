package com.senzhikong.enums;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum ActionStatus {
    SUCCESS("success", "成功"),
    FAIL("fail", "失败"),
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    ActionStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
