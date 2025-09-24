package com.spark.notification_service.rabbitmq;

import com.spark.notification_service.dto.OrderNotification;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    private final RabbitTemplate rabbitTemplate;

    public NotificationListener(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = RabbitMQConfig.NOTIFICATION_QUEUE)
    public void handleNotification(OrderNotification notify){
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_CONFIRM_QUEUE, notify);
    }
}
