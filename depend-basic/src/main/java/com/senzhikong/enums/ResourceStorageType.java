package com.senzhikong.enums;

/**
 * @author shu
 */
public enum ResourceStorageType {
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

    private String code;
    private String description;

    ResourceStorageType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
