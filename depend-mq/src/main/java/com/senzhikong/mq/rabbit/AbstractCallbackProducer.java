package com.senzhikong.mq.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author shu
 */
@Slf4j
public abstract class AbstractCallbackProducer extends MsgProducer implements RabbitTemplate.ConfirmCallback, InitializingBean {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 回调id:" + correlationData);
        if (ack) {
            log.info("消息成功消费");
            this.success();
        } else {
            log.info("消息消费失败:" + cause);
            this.fail();
        }
    }

    /**
     * 成功回调
     */
    public abstract void success();

    /**
     * 识别回调
     */
    public abstract void fail();

    @Override
    public void afterPropertiesSet() {
        rabbitTemplate.setConfirmCallback(this);
        //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
    }
}
