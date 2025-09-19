package com.spark.product_service.event;

import com.spark.product_service.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class ProductEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ProductEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Value("${custom.rabbit.exchange}")
    private String exchange;

    public void publishCreated(Product p){
        Map<String, Object> payload = Map.of(
                "event", "PRODUCT_CREATED",
                "id", p.getId(),
                "name", p.getName(),
                "price", p.getPrice(),
                "stock", p.getStock()
        );
        rabbitTemplate.convertAndSend(exchange, "product.created", payload);
        log.info("Product Created In RabbitMQ :{} ", payload.get("name"));
    }

    public void publishUpdates(Product p){
        Map<String, Object> payload = Map.of(
                "event", "PRODUCT_UPDATED",
                "id", p.getId(),
                "stock", p.getStock()
        );
        rabbitTemplate.convertAndSend(exchange, "product.updated", payload);
        log.info("Product Updated In RabbitMQ :{} ", payload.get("name"));
    }

    public void publishDeleted(Long id){
        Map<String, Object> payload = Map.of(
                "event", "PRODUCT_DELETED",
                "id", id
        );
        rabbitTemplate.convertAndSend(exchange, "product.deleted", payload);
        log.info("Product Deleted In RabbitMQ :{} ", payload.get("name"));
    }

}
