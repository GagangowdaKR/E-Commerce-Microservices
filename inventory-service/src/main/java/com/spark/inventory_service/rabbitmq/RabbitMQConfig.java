package com.spark.inventory_service.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    // Exchange & Routing Keys
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String INVENTORY_ROUTING_KEY = "inventory.routing.key";

    public static final String ORDER_CHECK_QUEUE = "order.check.queue";
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_ROUTING_KEY = "order.routing.key";

    public static final String ORDER_STATUS_QUEUE = "order.status.queue";

    // Declare exchange
    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }
    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE);
    }
    @Bean
    public Binding inventoryBinding(){
        return BindingBuilder.bind(inventoryQueue())
                .to(inventoryExchange())
                .with(INVENTORY_ROUTING_KEY);
    }

    //****************************************************************
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }
    @Bean
    public Queue orderCheckQueue() {
        return new Queue(ORDER_CHECK_QUEUE);
    }
    @Bean
    public Binding orderCheckBinding() {
        return BindingBuilder.bind(orderCheckQueue())
                .to(orderExchange())
                .with(ORDER_ROUTING_KEY);
    }


    //*****************************************************************
    @Bean
    public Queue orderStatusQueue() {
        return new Queue(ORDER_STATUS_QUEUE);
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
