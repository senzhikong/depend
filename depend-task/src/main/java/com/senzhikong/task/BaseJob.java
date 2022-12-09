package com.senzhikong.task;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

/**
 * @author Shu.Zhou
 */
public abstract class BaseJob implements Job {

    public JSONObject getJobParam(JobExecutionContext context) {
        JobDataMap paramMap = context.getJobDetail()
                .getJobDataMap();
        return JSONObject.parseObject(paramMap.getString("data"));
    }
}
