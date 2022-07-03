package com.senzhikong.email;

import com.senzhikong.util.enums.BaseEnum;
import lombok.Getter;

/**
 * @author shu
 */
@Getter
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
    private final String description;

    EmailPriority(String level, String code, String description) {
        this.level = level;
        this.code = code;
        this.description = description;
    }
}
