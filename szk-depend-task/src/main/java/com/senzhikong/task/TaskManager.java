package com.senzhikong.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.senzhikong.spring.SpringContextHolder;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
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
@ConditionalOnProperty(prefix = "szk.task", name = "enabled", havingValue = "true")
public class TaskManager implements InitializingBean {
    private Map<String, JobDetail> taskMap = new HashMap<>(16);
    @Resource
    ApplicationContext applicationContext;
    @Value("${szk.task.init-clz}")
    private String initClz;
    @Value("${szk.task.group}")
    private String group;
    @Resource
    private Scheduler scheduler;
    private TaskClassUtil classUtil;
    private static final String NON_GROUP = "0";

    @SuppressWarnings("unchecked")
    public Class<? extends Job> getTaskClass(BaseTaskInfo task) {
        Class<?> taskClass;
        try {
            if (StringUtils.isNotBlank(task.getJavaFile())) {
                return (Class<? extends Job>) classUtil.getClassFromJavaFile(task);
            }
            taskClass = Class.forName(task.getTaskClass());
            return (Class<? extends Job>) taskClass;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public void startJob(BaseTaskInfo task) {
        log.info("新增定时任务【{}】【{}】【{}】【{}】",task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getCronExpression());
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("data", task.getTaskParam());
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
            log.error("新增定时任务失败【{}】【{}】【{}】【{}】",task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getTaskCode());
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
        log.info("移除定时任务【{}】【{}】【{}】【{}】",task.getTaskName(), task.getGroupCode(), task.getTaskCode(), task.getTaskCode());
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
        JobKey jobKey = new JobKey(task.getTaskCode(), task.getGroupCode());
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            Class<? extends Job> taskClass = getTaskClass(task);
            jobDetail = JobBuilder.newJob(taskClass)
                    .withIdentity(task.getTaskCode(), task.getGroupCode())
                    .storeDurably()
                    .usingJobData(dataMap)
                    .build();
            scheduler.addJob(jobDetail, true);
        }
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), "run-one-time");
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger == null) {
            trigger = TriggerBuilder.newTrigger()
                    .withIdentity(TriggerKey.triggerKey(task.getTaskCode(),
                            "run-one-time" + System.currentTimeMillis()))
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                    .forJob(jobDetail)
                    .startAt(new Date())
                    .build();
            // 启动
            scheduler.scheduleJob(trigger);
        } else {
            scheduler.triggerJob(jobKey);
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
    public TaskStatus getTaskStatus(BaseTaskInfo task) throws Exception {
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        TriggerState state = scheduler.getTriggerState(triggerKey);
        return switch (state) {
            case BLOCKED -> TaskStatus.BLOCKED;
            case COMPLETE -> TaskStatus.COMPLETE;
            case ERROR -> TaskStatus.ERROR;
            case PAUSED -> TaskStatus.PAUSED;
            case NORMAL -> TaskStatus.NORMAL;
            case NONE -> TaskStatus.NONE;
            default -> null;
        };
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.debug("-------------------初始化定时任务-------------------");
        classUtil = new TaskClassUtil(applicationContext);
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
