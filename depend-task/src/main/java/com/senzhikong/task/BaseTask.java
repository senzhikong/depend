package com.senzhikong.task;

import java.util.Date;

/**
 * @author shu
 */
public interface BaseTask {
    /**
     * 获取任务编号
     *
     * @return 任务编号
     */
    String getTaskCode();

    /**
     * 获取任务分组
     *
     * @return 任务分组
     */
    String getGroupCode();

    /**
     * 获取任务实现类
     *
     * @return 任务编实现类
     */
    String getTaskClass();

    /**
     * 获取任cron表达式
     *
     * @return cron表达式
     */
    String getCronExpression();

    /**
     * 获取任务状态
     *
     * @return 任务状态
     */
    String getStatus();

    /**
     * 设置任务状态
     *
     * @param status 任务状态
     */
    void setStatus(String status);

    /**
     * 获取任务参数
     *
     * @return 任务参数
     */
    String getTaskParam();

    /**
     * 获取任务描述
     *
     * @return 任务描述
     */
    String getDescription();

    /**
     * 设置任务描述
     *
     * @param description 任务描述
     */
    void setDescription(String description);

    /**
     * 获取任务创建时间
     *
     * @return 任务创建时间
     */
    Date getCreateTime();

    /**
     * 设置任务创建时间
     *
     * @param createTime 任务创建时间
     */
    void setCreateTime(Date createTime);

    /**
     * 获取cron表达式描述
     *
     * @return cron表达式描述
     */
    String getCronDescription();

    /**
     * 获取任务名称
     *
     * @return 任务名称
     */
    String getTaskName();

    /**
     * 获取任务分组名称
     *
     * @return 任务分组名称
     */
    String getGroupName();

    /**
     * 获取是否自动启动
     *
     * @return 是否自启
     */
    Boolean getAutoStart();

    /**
     * 设置是否自启
     *
     * @param autoStart 是否自启
     */
    void setAutoStart(Boolean autoStart);

    /**
     * 获取java文件内容
     *
     * @return 任务编号
     */
    String getJavaFile();

    /**
     * 设置运行状态
     *
     * @param runningStatus 运行状态
     */
    void setRunningStatus(String runningStatus);

    /**
     * 设置下一次触发时间
     *
     * @param nextFireTime 下一次触发时间
     */
    void setNextFireTime(Date nextFireTime);

    /**
     * 设置运行状态描述
     *
     * @param runningStatusDesc 运行状态描述
     */
    void setRunningStatusDesc(String runningStatusDesc);
}
