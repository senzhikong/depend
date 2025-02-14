package com.senzhikong.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.senzhikong.spring.SpringContextHolder;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Shu.zhou
 */
@Slf4j
@Getter
@Setter
@Component
public class TaskManager implements ApplicationListener<ApplicationStartedEvent> {
    private Map<String, JobDetail> taskMap = new HashMap<>(16);

    @Value("${szk.task.init-clz}")
    private String initClz;
    @Value("${szk.task.group}")
    private String group;
    @Value("${szk.task.enabled}")
    private boolean enabled;
    @Resource
    private Scheduler scheduler;
    private static final String NON_GROUP = "0";

    @SuppressWarnings("unchecked")
    public Class<? extends Job> getTaskClass(BaseTaskInfo task) {
        Class<?> taskClass;
        try {
            taskClass = Class.forName(task.getTaskClass());
            return (Class<? extends Job>) taskClass;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void startJob(BaseTaskInfo task) {
        log.info("新增定时任务【{}】【{}】【{}】【{}】", task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getCronExpression());
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("data", task.getTaskParam());
            dataMap.put("autoRun", true);
            JobDetail jobDetail = JobBuilder.newJob(getTaskClass(task))
                    .withIdentity(task.getTaskCode(), task.getGroupCode())
                    .storeDurably()
                    .usingJobData(dataMap)
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(task.getTaskCode(), task.getGroupCode())
                    .withSchedule(scheduleBuilder)
                    .forJob(jobDetail)
                    .build();
            scheduler.addJob(jobDetail, true);
            // 启动
            scheduler.scheduleJob(trigger);
            taskMap.put(task.getTaskCode(), jobDetail);
        } catch (Exception e) {
            log.error("新增定时任务失败【{}】【{}】【{}】【{}】", task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getTaskCode());
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 移除任务
     *
     * @param task 任务
     * @throws Exception 异常
     */
    public void removeJob(BaseTaskInfo task) throws Exception {
        log.info("移除定时任务【{}】【{}】【{}】【{}】", task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getTaskCode());
        // TriggerKey
        JobKey jobKey = new JobKey(task.getTaskCode(), task.getGroupCode());
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(jobKey);
        taskMap.remove(task.getTaskCode());
    }


    /**
     * 运行一次任务
     *
     * @param task 任务
     * @throws Exception 异常
     */
    public void runJob(BaseTaskInfo task) throws Exception {
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("data", task.getTaskParam());
        dataMap.put("autoRun", false);
        dataMap.put("runBy", task.getRunBy());
        String taskCode = task.getTaskCode();
        String groupCode = "run-one-time";
        JobKey jobKey = new JobKey(taskCode, groupCode);
        Class<? extends Job> taskClass = getTaskClass(task);
        JobDetail jobDetail = JobBuilder.newJob(taskClass)
                .withIdentity(taskCode, groupCode)
                .storeDurably()
                .usingJobData(dataMap)
                .build();
        scheduler.addJob(jobDetail, true);

        TriggerKey triggerKey = new TriggerKey(taskCode, groupCode);
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(triggerKey)
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .forJob(jobDetail)
                    .usingJobData(dataMap)
                    .startAt(new Date())
                    .build();
            // 启动
            scheduler.scheduleJob(trigger);
        } else {
            scheduler.triggerJob(jobKey, dataMap);
        }
    }

    public void startJobs() {
        try { // 启动
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 关闭所有任务
     */
    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取任务下一次触发时间
     *
     * @param task 任务
     * @return 下一次触发时间
     * @throws Exception 异常
     */
    public Date getNextFireTime(BaseTaskInfo task) throws Exception {
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            return trigger.getNextFireTime();
        }
        return null;
    }

    /**
     * 获取任务状态
     *
     * @param task 任务
     * @return 状态
     * @throws Exception 异常
     */
    public TaskRunStatus getTaskStatus(BaseTaskInfo task) throws Exception {
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        TriggerState state = scheduler.getTriggerState(triggerKey);
        return switch (state) {
            case BLOCKED -> TaskRunStatus.BLOCKED;
            case COMPLETE -> TaskRunStatus.COMPLETE;
            case ERROR -> TaskRunStatus.ERROR;
            case PAUSED -> TaskRunStatus.PAUSED;
            case NORMAL -> TaskRunStatus.NORMAL;
            case NONE -> TaskRunStatus.NONE;
        };
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent event) {
        if (!enabled) {
            return;
        }
        log.debug("-------------------初始化定时任务-------------------");
        if (StringUtils.isBlank(initClz)) {
            return;
        }
        if (StringUtils.isBlank(group)) {
            return;
        }
        String[] groups = group.split(",");
        if (groups.length == 0) {
            return;
        }
        if (StringUtils.equals(NON_GROUP, group)) {
            groups = null;
        }

        List<BaseTaskInfo> initTaskList = new ArrayList<>();
        try {
            Object clz = SpringContextHolder.getBeanByClassName(initClz);
            Method method = clz.getClass()
                    .getMethod("listAutoStartTask", String[].class);
            Object res = method.invoke(clz, new Object[]{groups});
            List<BaseTaskInfo> list = JSONArray.parseArray(JSON.toJSONString(res), BaseTaskInfo.class);
            if (list != null && !list.isEmpty()) {
                initTaskList.addAll(list);
            }
        } catch (Exception e) {
            throw new RuntimeException("定时任务初始换失败", e);
        }
        for (BaseTaskInfo task : initTaskList) {
            try {
                startJob(task);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        startJobs();
        log.debug("-------------------定时任务初始化完成-------------------");
    }
}
