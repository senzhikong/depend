package com.senzhikong.basic.enums;

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
public enum ActionStatus implements BaseEnum {
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
    private final String desc;

}
