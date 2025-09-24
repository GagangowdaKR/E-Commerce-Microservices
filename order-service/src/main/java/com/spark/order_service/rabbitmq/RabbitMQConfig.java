package com.spark.order_service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // To check the availability of stock
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.routing.key";
    public static final String ORDER_CHECK_QUEUE = "order.check.queue";

    // To place the product id and stock into inventory db
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String INVENTORY_ROUTING_KEY = "inventory.routing.key";

    public static final String ORDER_STATUS_QUEUE = "order.status.queue";
    public static final String PAYMENT_QUEUE = "payment.queue"; // -> notification-service

    public static final String ORDER_CONFIRM_QUEUE = "order.confirmed.queue"; // final

    @Bean
    public TopicExchange orderExchange(){
        return new TopicExchange(ORDER_EXCHANGE);
    }
    @Bean
    public Queue orderChackQueue(){
        return new Queue(ORDER_CHECK_QUEUE);
    }
    @Bean
    public Queue orderStatusQueue(){
        return new Queue(ORDER_STATUS_QUEUE);
    }
    @Bean
    public Queue orderConfirmedQueue(){
        return new Queue(ORDER_CONFIRM_QUEUE);
    }

    @Bean
    public Queue paymentQueue(){
        return new Queue(PAYMENT_QUEUE);
    }
    @Bean
    public Binding orderBinding(){
        return BindingBuilder.bind(orderChackQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory factory){
        RabbitTemplate template = new RabbitTemplate(factory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}