package com.senzhikong.mq;

/**
 * @author shu
 */
public abstract class AbstractMsgReceiver {
    /**
     * 具体业务
     *
     * @param content 消息内容
     */
    public abstract void process(String content);

    /**
     * 获取队列名称
     *
     * @return 队列名称
     */
    public abstract String getQueue();
}
