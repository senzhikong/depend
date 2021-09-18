package com.senzhikong.task;

import java.util.Date;

public interface BaseTask {

    public String getTaskCode();

    public void setTaskCode(String taskCode);

    public String getGroupCode();

    public void setGroupCode(String groupCode);

    public String getTaskClass();

    public void setTaskClass(String taskClass);

    public String getCronExpression();

    public void setCronExpression(String cronExpression);

    public String getStatus();

    public void setStatus(String status);

    public String getTaskParam();

    public void setTaskParam(String taskParam);

    public String getDescription();

    public void setDescription(String description);

    public Date getCreateTime();

    public void setCreateTime(Date createTime);

    public String getCronDescription();

    public void setCronDescription(String cronDescription);

    public String getTaskName();

    public void setTaskName(String taskName);

    public String getGroupName();

    public void setGroupName(String groupName);

    public Boolean getAutoStart();

    public void setAutoStart(Boolean autoStart);

    public String getJavaFile();

    public void setJavaFile(String javaFile);


    public String getRunningStatus();

    public void setRunningStatus(String runningStatus);

    public Date getNextFireTime();

    public void setNextFireTime(Date nextFireTime);

    public String getRunningStatusDesc();

    public void setRunningStatusDesc(String runningStatusDesc);
}
