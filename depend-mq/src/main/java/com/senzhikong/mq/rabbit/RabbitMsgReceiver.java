package com.senzhikong.mq.rabbit;

import com.rabbitmq.client.*;
import com.senzhikong.mq.AbstractMsgReceiver;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

/**
 * @author shu
 */
@Getter
@Setter
@Slf4j
public abstract class RabbitMsgReceiver extends AbstractMsgReceiver implements InitializingBean {
    @Resource
    ConnectionFactory factory;
    private Connection connection;
    private Channel channel;

    /**
     * 具体业务
     *
     * @param content 消息内容
     */
    public abstract void process(String content);

    public abstract String getQueue();

    @Override
    public void afterPropertiesSet() throws Exception {
        this.open();
    }

    public void open() throws Exception {
        if (this.connection != null || this.channel != null) {
            return;
        }
        this.connection = factory.createConnection();
        // 创建信道
        this.channel = connection.createChannel(false);
        // 设置客户端最多接受未被ack的消息的个数
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                    AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body);
                log.info("收到消息：" + msg);
                process(msg);
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };
        channel.basicConsume(getQueue(), consumer);
    }

    public void close() {
        if (this.connection != null) {
            this.connection.close();
            this.connection = null;
        }
        if (this.channel != null) {
            try {
                this.channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.channel = null;
        }
    }
}
