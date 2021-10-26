package com.senzhikong.enums;

import lombok.Getter;

@Getter
public enum AccountType {
    MEMBER("member", "会员"),
    ADMIN("admin", "管理员"),
    PARTNER("partner", "合作伙伴"),
    UNKNOWN("unknown", "位置");

    private final String code;
    private final String description;

    AccountType(String code, String description) {
        this.code = code;
        this.description = description;
    }

}
