package com.spark.order_service.rabbitmq;

import com.spark.order_service.dto.OrderCheckResponse;
import com.spark.order_service.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusListener {


    private final OrderService orderService;

    public  OrderStatusListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_STATUS_QUEUE)
    public void receiveOrderStatus(OrderCheckResponse statusMessage) {
        System.out.println("order id : " + statusMessage.getOrderId());
        System.out.println("order status : " + statusMessage.getStatus());

        orderService.updateOrderStatus(statusMessage);
    }
}

