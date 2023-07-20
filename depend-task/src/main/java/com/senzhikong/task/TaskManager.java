package com.senzhikong.task;

import com.senzhikong.module.InitializeBean;
import com.senzhikong.spring.SpringContextHolder;
import com.senzhikong.util.string.StringUtil;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.*;
import org.quartz.Trigger.TriggerState;
import org.springframework.context.ApplicationContext;

import jakarta.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Shu.zhou
 */
@Getter
@Setter
public class TaskManager implements InitializeBean {
    private static final Log logger = LogFactory.getLog(TaskManager.class);
    private Map<String, JobDetail> taskMap;
    @Resource
    ApplicationContext applicationContext;
    private String initClz;
    private String group;
    @Resource
    private Scheduler scheduler;
    private TaskClassUtil classUtil;

    public TaskManager() {
        taskMap = new HashMap<>();
    }

    @SuppressWarnings("unchecked")
    public Class<? extends Job> getTaskClass(BaseTask task) {
        Class<?> taskClass;
        try {
            if (StringUtil.isNotEmpty(task.getJavaFile())) {
                return (Class<? extends Job>) classUtil.getClassFromJavaFile(task);
            }
            taskClass = Class.forName(task.getTaskClass());
            return (Class<? extends Job>) taskClass;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
        return null;
    }

    public void addTask(BaseTask task) throws Exception {
        addTask(task.getTaskCode(), task.getGroupCode(), getTaskClass(task), task.getCronExpression(),
                task.getTaskParam());
    }

    public void addTask(String taskCode, String groupCode, Class<? extends Job> cls, String cron, String data) {
        logger.info("新增定时任务——" + groupCode + "" + taskCode + "" + cron + "" + cls.getName());
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("data", data);
        try {
            JobDetail jobDetail = JobBuilder.newJob(cls)
                    .withIdentity(taskCode, groupCode)
                    .storeDurably()
                    .usingJobData(dataMap)
                    .build();
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(taskCode, groupCode)
                    .withSchedule(scheduleBuilder)
                    .forJob(jobDetail)
                    .build();
            scheduler.addJob(jobDetail, true);
            // 启动
            scheduler.scheduleJob(trigger);
            taskMap.put(taskCode, jobDetail);
        } catch (Exception e) {
            logger.error(e);
        }
    }

    public void removeJob(BaseTask task) throws Exception {
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

    public void pauseJob(BaseTask task) throws Exception {
        // TriggerKey
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
    }

    public void startJob(BaseTask task) throws Exception {
        addTask(task);
        startJobs();
    }

    public void runJob(BaseTask task) throws Exception {
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

    public void shutdownJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BaseTask getTaskStatus(BaseTask task) throws Exception {
        TriggerKey triggerKey = new TriggerKey(task.getTaskCode(), task.getGroupCode());
        Trigger trigger = scheduler.getTrigger(triggerKey);
        if (trigger != null) {
            task.setNextFireTime(trigger.getNextFireTime());
        }
        TriggerState state = scheduler.getTriggerState(triggerKey);
        switch (state) {
            case BLOCKED:
                task.setRunningStatus(TaskStatus.BLOCKED.getCode());
                task.setRunningStatusDesc(TaskStatus.BLOCKED.getDescription());
                break;
            case COMPLETE:
                task.setRunningStatus(TaskStatus.COMPLETE.getCode());
                task.setRunningStatusDesc(TaskStatus.COMPLETE.getDescription());
                break;
            case ERROR:
                task.setRunningStatus(TaskStatus.ERROR.getCode());
                task.setRunningStatusDesc(TaskStatus.ERROR.getDescription());
                break;
            case PAUSED:
                task.setRunningStatus(TaskStatus.PAUSED.getCode());
                task.setRunningStatusDesc(TaskStatus.PAUSED.getDescription());
                break;
            case NORMAL:
                task.setRunningStatus(TaskStatus.NORMAL.getCode());
                task.setRunningStatusDesc(TaskStatus.NORMAL.getDescription());
                break;
            case NONE:
                task.setRunningStatus(TaskStatus.NONE.getCode());
                task.setRunningStatusDesc(TaskStatus.NONE.getDescription());
                break;
            default:
                break;
        }
        return task;
    }

    private static final String NON_GROUP = "0";

    @Override
    public void init() {
        logger.debug("-------------------初始化定时任务-------------------");
        classUtil = new TaskClassUtil(applicationContext);
        if (StringUtil.isEmpty(initClz)) {
            return;
        }
        if (StringUtil.isEmpty(group)) {
            return;
        }
        String[] groups = group.split(",");
        if (groups.length == 0) {
            return;
        }
        if (StringUtil.equal(NON_GROUP, group)) {
            groups = null;
        }

        List<BaseTask> initTaskList = new ArrayList<>();
        try {
            Object clz = SpringContextHolder.getBeanByClassName(initClz);
            Method method = clz.getClass()
                    .getMethod("listAutoStartTask", String[].class);
            @SuppressWarnings("unchecked") List<BaseTask> list = (List<BaseTask>) method.invoke(clz,
                    new Object[]{groups});
            if (list != null && list.size() > 0) {
                initTaskList.addAll(list);
            }

        } catch (Exception e) {
            throw new RuntimeException("定时任务自启失败", e);
        }
        for (BaseTask task : initTaskList) {
            try {
                addTask(task);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        startJobs();
        logger.debug("-------------------定时任务初始化完成-------------------");
    }

}
