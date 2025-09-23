package com.spark.product_service.event;

import com.spark.product_service.dto.InventoryMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RabbitMQProducer {

    private RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToInventory(InventoryMessage message) {
        rabbitTemplate.convertAndSend("inventory.exchange", "inventory.routing.key", message);
    }
}
