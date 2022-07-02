package com.senzhikong.enums;

import lombok.Getter;

/**
 * 审核状态
 *
 * @author Shu.Zhou
 */
@Getter
public enum OrderType {
    /**
     * 正序
     */
    ASC("asc", "正序"),
    /**
     * 倒序
     */
    DESC("desc", "倒序");

    private final String code;
    private final String description;

    OrderType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
