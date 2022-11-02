package com.myhr.config;

import com.myhr.common.constant.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    /**
     * @Description 该队列用于处理 各系统角色信息
     * */
    @Bean
    public Queue roleQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        //   return new Queue("TestDirectQueue",true,true,false);

        //一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(RabbitMqConstants.AUTH_ROLE_QUEUE_NEW, true);
    }

    @Bean
    public Queue roleQueue2() {
        return new Queue(RabbitMqConstants.AUTH_ROLE_QUEUE2, true);
    }

    /**
     * @Description DirectExchange 直连交换机，根据消息携带的路由键将消息投递给对应队列
     *
     * */
    @Bean
    public DirectExchange authRoleExchange() {
        return new DirectExchange(RabbitMqConstants.AUTH_ROLE_EXCHANGE, true, false);
    }

    /**
     * @Description 将队列绑定到交换机上
     *
     * */
    @Bean
    public Binding binding(DirectExchange authRoleExchange) {
        //将队列绑定到交换机，且使用 队列名称 作为binding key
        return BindingBuilder.bind(roleQueue()).to(authRoleExchange).withQueueName();
    }
    @Bean
    public Binding binding2(DirectExchange authRoleExchange) {
        //将队列绑定到交换机，且使用 队列名称 作为binding key
        return BindingBuilder.bind(roleQueue2()).to(authRoleExchange).withQueueName();
    }



    /**
     * 自定义MessagePostProcessor 以便接收消息时能获取到CorrelationId
     * */
    @Bean
    public MessagePostProcessor correlationIdProcessor() {
        MessagePostProcessor messagePostProcessor = new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message, Correlation correlation) {
                MessageProperties messageProperties = message.getMessageProperties();

                if (correlation instanceof CorrelationData) {
                    String correlationId = ((CorrelationData) correlation).getId();
                    messageProperties.setCorrelationId(correlationId);
                }
                return message;
            }

            @Override
            public Message postProcessMessage(Message message) {
                return message;
            }
        };
        return messagePostProcessor;
    }

}
