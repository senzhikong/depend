package com.senzhikong.basic.enums;

import com.senzhikong.util.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author shu
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public enum AccountType implements BaseEnum {
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
    private final String desc;
}
