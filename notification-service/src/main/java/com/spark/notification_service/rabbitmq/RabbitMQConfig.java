package com.spark.notification_service.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String ORDER_CONFIRM_QUEUE = "order.confirmed.queue";

    @Bean
    public Queue notificationQueue(){
        return new Queue(NOTIFICATION_QUEUE);
    }
    @Bean
    public Queue orderConfirmedQueue(){
        return new Queue(ORDER_CONFIRM_QUEUE);
    }
    @Bean
    public MessageConverter  messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}
