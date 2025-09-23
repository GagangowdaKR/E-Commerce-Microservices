package com.spark.inventory_service.rabbitmq;

import com.spark.inventory_service.dto.OrderCheckRequest;
import com.spark.inventory_service.dto.OrderCheckResponse;
import com.spark.inventory_service.entity.Inventory;
import com.spark.inventory_service.repository.InventoryRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OrderCheckListener {

    private final InventoryRepository inventoryRepo;
    private final RabbitTemplate rabbitTemplate;

    public OrderCheckListener(InventoryRepository repo, RabbitTemplate template){
        this.inventoryRepo = repo;
        this.rabbitTemplate = template;
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CHECK_QUEUE)
    public void handleOrderCheckRequest(OrderCheckRequest orderRequest){
        boolean allAvailable = true;

        for(Map.Entry<Long, Integer> obj : orderRequest.getProductQuantities().entrySet()){
            Long productId = obj.getKey();
            Integer requestedQty = obj.getValue();

            Inventory inventory = inventoryRepo.findById(productId).orElse(null);
            if(inventory == null || inventory.getStock() < requestedQty){
                allAvailable = false;
                break;
            }
        }

        OrderCheckResponse orderCheckResponse = new OrderCheckResponse();
        if(allAvailable){
            //Reduce the stock
            for(Map.Entry<Long, Integer> obj : orderRequest.getProductQuantities().entrySet()){
                Inventory inventory = inventoryRepo.findById(obj.getKey()).orElseThrow(() -> new RuntimeException("Product not found"));
                inventory.setStock(inventory.getStock() - obj.getValue());
                inventoryRepo.save(inventory);
            }
            orderCheckResponse.setStatus("COMPLETED");
        }else{
            orderCheckResponse.setStatus("FAILED");
        }
        orderCheckResponse.setOrderId(orderRequest.getOrderId());

        // Notify order-service
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_STATUS_QUEUE, orderCheckResponse);
    }
}