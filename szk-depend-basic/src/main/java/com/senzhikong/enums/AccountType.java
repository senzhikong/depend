package com.senzhikong.enums;

import lombok.Getter;

/**
 * @author shu
 */

@Getter
public enum AccountType {
    /**
     * 会员
     */
    MEMBER("member", "会员"),
    /**
     * 管理员
     */
    ADMIN("admin", "管理员"),
    /**
     * 合作伙伴
     */
    PARTNER("partner", "合作伙伴"),
    /**
     * 未知
     */
    UNKNOWN("unknown", "未知");

    private final String code;
    private final String description;

    AccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
