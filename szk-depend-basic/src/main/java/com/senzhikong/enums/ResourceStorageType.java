package com.senzhikong.enums;

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
public enum ResourceStorageType implements BaseEnum {
    /**
     * 本地存储
     */
    LOCAL("local", "本地存储"),
    /**
     * FTP存储
     */
    FTP("ftp", "FTP存储"),
    /**
     * 阿里云OSS
     */
    OSS("oss", "阿里云OSS"),
    /**
     * 腾讯云COS
     */
    COS("cos", "腾讯云COS"),
    /**
     * 七牛云Kodo
     */
    KODO("kodo", "七牛云Kodo");

    private final String code;
    private final String desc;
}
