package com.senzhikong.task;

import lombok.Data;

/**
 * @author shu
 */
@Data
public class BaseTaskInfo {
    /**
     * 任务编号
     */
    private String taskCode;
    /**
     * 任务分账
     */
    private String groupCode;
    /**
     * 任务执行类
     */
    private String taskClass;
    /**
     * 触发时间cron表达式
     */
    private String cronExpression;
    /**
     * 任务餐宿
     */
    private String taskParam;
    /**
     * java文件内容
     */
    private String javaFile;
}