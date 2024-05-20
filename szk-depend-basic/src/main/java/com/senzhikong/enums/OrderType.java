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
public enum OrderType implements BaseEnum {
    /**
     * 正序
     */
    ASC("asc", "正序"),
    /**
     * 倒序
     */
    DESC("desc", "倒序");

    private final String code;
    private final String desc;
}
