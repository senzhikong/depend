package com.senzhikong.task;

import jakarta.annotation.Resource;
import lombok.NonNull;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

/**
 * @author Shu.zhou
 * @date 2018年12月5日上午10:24:47
 */
public class TaskFactory extends AdaptableJobFactory {

    /**
     * 这个对象Spring会帮我们自动注入进来,也属于Spring技术范畴.
     */
    @Resource
    private AutowireCapableBeanFactory capableBeanFactory;

    @Override
    @NonNull
    protected Object createJobInstance(@NonNull TriggerFiredBundle bundle) throws Exception {
        // 调用父类的方法
        Object jobInstance = super.createJobInstance(bundle);
        // 进行注入,这属于Spring的技术,不清楚的可以查看Spring的API.
        capableBeanFactory.autowireBean(jobInstance);
        return jobInstance;
    }
}
