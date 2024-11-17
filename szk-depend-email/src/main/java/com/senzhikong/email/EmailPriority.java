package com.senzhikong.email;

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
public enum EmailPriority implements BaseEnum {
    /**
     * 缓慢
     */
    LOW("5", "Low", "缓慢"),
    /**
     * 普通
     */
    NORMAL("3", "Normal", "普通"),
    /**
     * 紧急
     */
    HIGH("1", "High", "紧急"),
    ;

    private final String level;
    private final String code;
    private final String desc;
}
