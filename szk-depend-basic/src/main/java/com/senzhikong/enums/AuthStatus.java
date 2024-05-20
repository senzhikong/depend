package com.senzhikong.enums;

import com.senzhikong.util.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * 审核状态
 *
 * @author Shu.Zhou
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public enum AuthStatus implements BaseEnum {
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
    private final String desc;
}
