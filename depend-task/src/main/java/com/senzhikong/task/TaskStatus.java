package com.senzhikong.task;

/**
 * @author Shu.Zhou
 */
public enum TaskStatus {
    BLOCKED("blocked", "阻塞"),
    COMPLETE("complete", "完成"),
    ERROR("error", "错误"),
    PAUSED("pause", "暂停"),
    NORMAL("normal", "正常"),
    NONE("none", "未运行");

    private String code;
    private String description;

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
