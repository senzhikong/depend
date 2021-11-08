package com.senzhikong.task;

import lombok.Getter;

/**
 * @author Shu.Zhou
 */
@Getter
public enum TaskStatus {
    BLOCKED("blocked", "阻塞"),
    COMPLETE("complete", "完成"),
    ERROR("error", "错误"),
    PAUSED("pause", "暂停"),
    NORMAL("normal", "正常"),
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
