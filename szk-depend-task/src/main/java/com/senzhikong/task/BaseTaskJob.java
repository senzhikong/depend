package com.senzhikong.task;

import com.alibaba.fastjson.JSONObject;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author Shu.Zhou
 */
public abstract class BaseTaskJob implements Job {
    private static final String PARAM_KEY = "data";
    private static final String AUTO_RUN_KEY = "autoRun";
    private static final String RUN_BY = "runBy";


    /**
     * 获取任务执行参数
     *
     * @param context 定时任务上下文
     * @return 执行参数
     */
    public JSONObject getJobParam(JobExecutionContext context) {
        JobDataMap paramMap = context.getJobDetail().getJobDataMap();
        if (paramMap.getString(PARAM_KEY) == null) {
            return null;
        }
        return JSONObject.parseObject(paramMap.getString(PARAM_KEY));
    }

    public boolean autoRun(JobExecutionContext context) {
        JobDataMap paramMap = context.getJobDetail().getJobDataMap();
        return paramMap.getBoolean(AUTO_RUN_KEY);
    }

    public String runBy(JobExecutionContext context) {
        JobDataMap paramMap = context.getJobDetail().getJobDataMap();
        return paramMap.getString(RUN_BY);
    }
}
