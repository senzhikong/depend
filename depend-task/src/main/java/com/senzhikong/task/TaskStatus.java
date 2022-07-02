package com.senzhikong.task;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum TaskStatus {
    /**
     * 阻塞
     */
    BLOCKED("blocked", "阻塞"),
    /**
     * 完成
     */
    COMPLETE("complete", "完成"),
    /**
     * 错误
     */
    ERROR("error", "错误"),
    /**
     * 暂停
     */
    PAUSED("pause", "暂停"),
    /**
     * 正常
     */
    NORMAL("normal", "正常"),
    /**
     * 未运行
     */
    NONE("none", "未运行");

    private final String code;
    private final String description;

    TaskStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String code() {
        return code;
    }

    public String description() {
        return description;
    }

}
