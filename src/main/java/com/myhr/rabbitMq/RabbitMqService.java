package com.myhr.rabbitMq;

/**
 * @Description
 * @Author yyf
 * @Date 2022-10-13 11:48
 */
public interface RabbitMqService {
    void sendMessage(String exchange, String routingKey, String message);

    void sendMessage(String exchange, String routingKey, MessageContent messageContent);
}
