package com.senzhikong.task;

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
public enum TaskRunStatus implements BaseEnum {
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
    private final String desc;

}
