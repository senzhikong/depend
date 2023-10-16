package com.senzhikong.mq;

/**
 * @author shu
 */
public abstract class AbstractMsgReceiver {
    public abstract void process(String content);

    public abstract String getQueue();
}
