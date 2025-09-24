package com.spark.order_service.rabbitmq;

import com.spark.order_service.dto.OrderCheckResponse;
import com.spark.order_service.entity.Order;
import com.spark.order_service.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderFinalizeListener {

    private final OrderRepository orderRepo;

    public OrderFinalizeListener(OrderRepository orderRepo) {
        this.orderRepo = orderRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CONFIRM_QUEUE)
    public void handleOrderFinalize(OrderCheckResponse response){
        Order order = orderRepo.findById(response.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order Not Found, Database might be corrupted"));

        if(response.getStatus().equalsIgnoreCase("PAID"))
            order.setStatus("COMPLETED");
        else if(response.getStatus().equalsIgnoreCase("NOT_PAID"))
            order.setStatus("FAILED");
        orderRepo.save(order);

        log.info("Order Request has been completed here, Check the status below ");
        System.out.println("Order Status : "+ order.getStatus());
        System.out.println("Order Id : "+order.getId());
        System.out.println("Order Num : "+order.getOrderNumber());
        System.out.println("Order Status : "+order.getStatus());
    }
}
