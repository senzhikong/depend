package com.senzhikong.mq.rabbit;

import com.senzhikong.mq.AbstractMsgProducer;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author shu
 */
@Getter
@Setter
@Slf4j
@Component
@ConditionalOnProperty(prefix = "mq", name = "type", havingValue = "rabbit")
public class RabbitMsgProducer extends AbstractMsgProducer {

    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Override
    public void sendMsg(String content, String exchange, String routingKey) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routingKey, content, correlationId);
    }
}
