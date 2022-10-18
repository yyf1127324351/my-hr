package com.myhr.rabbitMq;

import com.myhr.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMqServiceImpl implements RabbitMqService ,InitializingBean {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    MessagePostProcessor correlationIdProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Override
    public void sendMessage(String exchange, String routingKey, String message) {
        String msgId = RandomStringUtils.randomAlphabetic(32);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationIdProcessor,new CorrelationData(msgId));
            log.info("发送队列消息成功，EXCHANGE:{}, ROUTING_KEY:{}, msgId:{},内容:{}", exchange, routingKey, msgId, message);
        }catch (Exception e) {
            log.error("发送队列消息报错，报错信息：" + e.getMessage(), e);
        }

    }

    @Override
    public void sendMessage(String exchange, String routingKey, MessageContent messageContent) {

        try {
            String message = JsonUtils.toJson(messageContent);
            String msgId = messageContent.getId();
            CorrelationData correlationData = new CorrelationData(msgId);
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);
            log.info("发送队列消息成功，EXCHANGE:{}, ROUTING_KEY:{}, msgId:{}, 内容:{}", exchange, routingKey, msgId, message);
        }catch (Exception e) {
            log.error("发送队列消息报错，报错信息：" + e.getMessage(), e);
        }

    }


}
