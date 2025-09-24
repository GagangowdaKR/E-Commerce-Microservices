package com.spark.inventory_service.rabbitmq;

import com.spark.inventory_service.dto.OrderCheckRequest;
import com.spark.inventory_service.dto.OrderCheckResponse;
import com.spark.inventory_service.entity.Inventory;
import com.spark.inventory_service.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
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
                log.info("Its a break due to less stock");
                System.out.println("Currently We Don't have that QTY, Order within : "+inventory.getStock());
                break;
            }
        }

        OrderCheckResponse orderCheckResponse = new OrderCheckResponse();
        orderCheckResponse.setStatus("NOT_AVAILABLE");
        if(allAvailable){
            //Reduce the stock
            for(Map.Entry<Long, Integer> obj : orderRequest.getProductQuantities().entrySet()){
                Inventory inventory = inventoryRepo.findById(obj.getKey()).orElseThrow(() -> new RuntimeException("Product not found"));
                inventory.setStock(inventory.getStock() - obj.getValue());
                inventoryRepo.save(inventory);
            }
            orderCheckResponse.setStatus("AVAILABLE");
        }
        orderCheckResponse.setUserId(orderRequest.getUserId());
        orderCheckResponse.setOrderId(orderRequest.getOrderId());

        // Notify order-service
        rabbitTemplate.convertAndSend(RabbitMQConfig.ORDER_STATUS_QUEUE, orderCheckResponse);
    }
}