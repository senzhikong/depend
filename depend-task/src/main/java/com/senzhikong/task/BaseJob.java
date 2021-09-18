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

    protected Log logger = LogFactory.getLog(this.getClass());

    public JSONObject getJobParam(JobExecutionContext context) {
        JobDataMap paremMap = context.getJobDetail()
                .getJobDataMap();
        JSONObject param = JSONObject.parseObject(paremMap.getString("data"));
        return param;
    }
}
