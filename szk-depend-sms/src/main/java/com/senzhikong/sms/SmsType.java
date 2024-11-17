package com.senzhikong.sms;

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
public enum SmsType implements BaseEnum {
    /**
     * 验证码
     */
    VERIFY_CODE("verify_code", "验证码");

    private final String code;
    private final String desc;
}
