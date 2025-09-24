package com.spark.order_service.service.impl;

import com.spark.order_service.dto.*;
import com.spark.order_service.entity.Order;
import com.spark.order_service.entity.OrderItem;
import com.spark.order_service.rabbitmq.RabbitMQConfig;
import com.spark.order_service.repository.OrderRepository;
import com.spark.order_service.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final RabbitTemplate rabbitTemplate;

    public OrderServiceImpl(OrderRepository orderRepo, RabbitTemplate rabbitTemplate){
        this.orderRepo = orderRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public OrderResponse placeOrder(PlaceOrderRequest request) {
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .userId(request.getUserId())
                .orderDate(LocalDateTime.now())
                .status("PENDING") // initially it is pending, it's before checking the stock
                .build();

        // stream to request.getItemList which contains (L productid, Int quantity) of OrderItemDto class
        // To convert into List of ItemList:(L prodId, I qty, Order order)
        order.setItemList(request.getItemList()
                .stream()
                .map(itemDto -> OrderItem.builder()
                        .productId(itemDto.getProductId())
                        .quantity(itemDto.getQuantity())
                        .order(order)
                        .build())
                .collect(Collectors.toList()));
        Order savedOrder = orderRepo.save(order);
        //  log.info("Order Place request has been sent, Status :- {}", savedOrder.getStatus());

        // Map<L,I> : stream to List<ItemItemDto> request.getItemList which contains (L productid, Int quantity) of OrderItemDto class
        Map<Long, Integer> productQuantities = request.getItemList()
                .stream()
                .collect(Collectors.toMap(OrderItemDto::getProductId, OrderItemDto::getQuantity));

        OrderCheckRequest msgToCheckOrderStock = OrderCheckRequest.builder()
                        .userId(savedOrder.getUserId())
                        .orderId(savedOrder.getId())
                        .productQuantities(productQuantities)
                        .build();

        // Msg to inventory to check stock is available : -> OrderCheckListener
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_ROUTING_KEY,
                msgToCheckOrderStock
        );

        return OrderResponse.builder()
                .orderNumber(savedOrder.getOrderNumber())
                .status("OPEN CONSOLE TO CHECK THE STATUS")
                .orderDate(savedOrder.getOrderDate())
                .itemList(request.getItemList())
                .build();
    }

    @Override
    public void updateOrderStatus(OrderCheckResponse orderCheckResponse) {
        // msg to -> payment
        rabbitTemplate.convertAndSend(RabbitMQConfig.PAYMENT_QUEUE, orderCheckResponse);
    }

    public List<Order> getAllOrders(){
        return orderRepo.findAll();
    }

    public Order getOrder(String orderNumber){
        return orderRepo.findByOrderNumber(orderNumber);
    }
}
