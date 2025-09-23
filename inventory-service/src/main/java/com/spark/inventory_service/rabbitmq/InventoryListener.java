package com.spark.inventory_service.rabbitmq;

import com.spark.inventory_service.dto.InventoryMessage;
import com.spark.inventory_service.entity.Inventory;
import com.spark.inventory_service.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryListener {

    private final InventoryRepository inventoryRepo;

    public InventoryListener(InventoryRepository inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    @RabbitListener(queues = RabbitMQConfig.INVENTORY_QUEUE)
    public void handleProductUpdate(InventoryMessage message){
        switch(message.getAction()){
            case "CREATE":
                Inventory inventory = new Inventory();
                inventory.setProductId(message.getProductId());
                inventory.setStock(message.getQuantity());
                inventoryRepo.save(inventory);
                break;
            case "UPDATE":
                Inventory inventory1 = inventoryRepo.findById(message.getProductId()).orElse(null);
                if(inventory1 == null){
                    log.info("Product is not in Inventory Table");
                    return;
                }
                inventory1.setStock(inventory1.getStock() + message.getQuantity());
                inventoryRepo.save(inventory1);
                break;
            case "DELETE":
                inventoryRepo.deleteById(message.getProductId());
                break;
        }
    }
}