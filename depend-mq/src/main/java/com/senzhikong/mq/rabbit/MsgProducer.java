package com.senzhikong.mq.rabbit;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

/**
 * @author shu
 */
@Slf4j
public class MsgProducer {
    private String exchange;
    private String routingKey;

    @Resource
    protected RabbitTemplate rabbitTemplate;

    public void sendMsg(String content) {
        //
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, content, correlationId);
    }
}
