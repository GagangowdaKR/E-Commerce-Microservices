package com.spark.order_service.service.impl;

import com.spark.order_service.dto.OrderCreatedEvent;
import com.spark.order_service.dto.OrderRequest;
import com.spark.order_service.dto.OrderResponse;
import com.spark.order_service.entity.Order;
import com.spark.order_service.entity.OrderItem;
import com.spark.order_service.rabbitmq.RabbitMQConfig;
import com.spark.order_service.repository.OrderRepository;
import com.spark.order_service.service.OrderService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepo;
    private AmqpTemplate amqpTemplate;

    public OrderServiceImpl(OrderRepository orderRepo, AmqpTemplate amqpTemplate){
        this.orderRepo = orderRepo;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public OrderResponse placeOrder(OrderRequest request) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .build();

        order.setItemList(request.getItemList().stream()
                .map(itemDto -> OrderItem.builder()
                        .productId(itemDto.getProductId())
                        .quantity(itemDto.getQuantity())
                        .order(order)
                        .build())
                .collect(Collectors.toList()));

        Order savedOrder = orderRepo.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                savedOrder.getOrderNumber(),
                savedOrder.getUserId(),
                request.getItemList()
        );

        amqpTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_CREATED_ROUTING_KEY,
                event
        );

        return OrderResponse.builder()
                .orderNumber(savedOrder.getOrderNumber())
                .status(savedOrder.getStatus())
                .orderDate(savedOrder.getOrderDate())
                .itemList(request.getItemList())
                .build();
    }


}
