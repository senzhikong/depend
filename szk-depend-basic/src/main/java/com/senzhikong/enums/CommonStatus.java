package com.senzhikong.enums;

import com.senzhikong.util.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author Shu.Zhou
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true, fluent = true)
public enum CommonStatus implements BaseEnum {
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
    private final String desc;
}
