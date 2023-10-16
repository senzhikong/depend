package com.senzhikong.mq;

/**
 * @author shu
 */
public abstract class AbstractMsgProducer {
    public abstract void sendMsg(String content, String exchange, String routingKey);
}
