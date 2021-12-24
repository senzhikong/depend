package com.senzhikong.mq.rabbit;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MsgReceiver implements InitializingBean {
    private String exchange;
    private String routingKey;
    private String queue;
    @Resource
    protected RabbitTemplate rabbitTemplate;
    @Resource
    ConnectionFactory factory;
    private Connection connection;

    public void process(String content) {
        log.info("接收处理队列A当中的消息： " + content);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.connection = factory.createConnection();
        final Channel channel = connection.createChannel(false);    // 创建信道
        channel.basicQos(64);    // 设置客户端最多接受未被ack的消息的个数
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
        channel.basicConsume(queue, consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }
}
