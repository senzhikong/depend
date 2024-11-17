package com.senzhikong.mq;

/**
 * @author shu
 */
public abstract class AbstractMsgProducer {
    /**
     * 发送消息
     *
     * @param content    消息内容
     * @param exchange   通道
     * @param routingKey 关键字
     */
    public abstract void sendMsg(String content, String exchange, String routingKey);
}
