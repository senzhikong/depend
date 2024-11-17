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
public enum SmsProvider implements BaseEnum {
    /**
     * 阿里云
     */
    ALIYUN("Aliyun", "阿里云");

    private final String code;
    private final String desc;
}
