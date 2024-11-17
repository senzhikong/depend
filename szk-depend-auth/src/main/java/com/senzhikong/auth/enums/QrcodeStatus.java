package com.senzhikong.auth.enums;

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
public enum QrcodeStatus implements BaseEnum {
    /**
     * 初始
     */
    INIT("init", "初始"),
    /**
     * 已扫
     */
    SCAN("scan", "已扫"),
    /**
     * 失败
     */
    FAIL("fail", "失败"),
    /**
     * 成功
     */
    SUCCESS("success", "成功"),
    /**
     * 过期
     */
    EXPIRED("expired", "过期"),
    /**
     * 异常
     */
    ERROR("error", "异常");

    private final String code;
    private final String desc;
}
