package com.myhr.rabbitMq;

import com.myhr.common.constant.RabbitMqConstants;
import com.myhr.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-13 15:47
 */

@Slf4j
@Component
public class RabbitConsumerOfRole {

//    @RabbitListener(queues = RabbitMqConstants.AUTH_ROLE_QUEUE)
//    @RabbitHandler
    public void consume(Message message) {
        String jsonMessage = new String(message.getBody(), Charset.defaultCharset());
        MessageContent messageContent = JsonUtils.toObject(jsonMessage,MessageContent.class);
        log.info("jsonMessage:" + jsonMessage);
        log.info("接受队列消息成功，队列:{}, msgId:{},内容:{}", RabbitMqConstants.AUTH_ROLE_QUEUE_NEW, messageContent.getId(), jsonMessage);
    }

//    @RabbitListener(queues = RabbitMqConstants.AUTH_ROLE_QUEUE2)
//    @RabbitHandler
    public void consume2(Message message) {
        String msgId = message.getMessageProperties().getCorrelationId();
        String data = new String(message.getBody(), Charset.defaultCharset());
        log.info("接受队列消息成功，队列:{}, msgId:{},内容:{}", RabbitMqConstants.AUTH_ROLE_QUEUE_NEW, msgId, data);
    }


    //监听死信队列
//    @RabbitListener(queues = "deadLetterQueue")
//    @RabbitHandler
    public void consumeDeadLetter(Message message) {
        String jsonMessage = new String(message.getBody(), Charset.defaultCharset());
        String jsonData = new String(Base64.getDecoder().decode(jsonMessage));
        log.info("死信队列：" + jsonData);
    }


}
