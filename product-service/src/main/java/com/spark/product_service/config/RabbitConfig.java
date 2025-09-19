package com.spark.product_service.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${custom.rabbit.exchange}")
    private String productExchange;

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(productExchange);
    }
}
