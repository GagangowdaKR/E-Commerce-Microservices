package com.spark.product_service.service.impl;

import com.spark.product_service.dto.InventoryMessage;
import com.spark.product_service.dto.ProductDto;
import com.spark.product_service.entity.Product;
import com.spark.product_service.event.RabbitMQProducer;
import com.spark.product_service.repository.ProductRepository;
import com.spark.product_service.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final RabbitMQProducer eventPublisher;

    public ProductServiceImpl(ProductRepository productRepo, RabbitMQProducer eventPublisher) {
        this.productRepo = productRepo;
        this.eventPublisher = eventPublisher;
    }

    public Product create(ProductDto productDto){
        Product product = new Product(productDto.getName(), productDto.getDescription(), productDto.getPrice());
        Product savedProd = productRepo.save(product);

        InventoryMessage msgToInventory = InventoryMessage.builder()
                .productId(savedProd.getId())
                .quantity(productDto.getStock())
                .action("CREATE")
                .build();

        log.info("Created product with id :- {} ",savedProd.getId());
        eventPublisher.sendToInventory(msgToInventory);
        log.info("Created MSG reached to RabbitMQ,:- {} ", msgToInventory);
        return savedProd;
    }

    public ResponseEntity<Product> update(Long id, ProductDto productDto){
        Product dbProduct = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
        dbProduct.setName(productDto.getName());
        dbProduct.setDescription(productDto.getDescription());
        dbProduct.setPrice(productDto.getPrice());

        Product savedProd = productRepo.save(dbProduct);

        InventoryMessage msgToInventory = InventoryMessage.builder()
                        .productId(savedProd.getId())
                        .quantity(productDto.getStock())
                        .action("UPDATE")
                        .build();

        log.info("Updated product with id :- {} ", savedProd.getId());
        eventPublisher.sendToInventory(msgToInventory);
        log.info("Updated MSG reached to RabbitMQ, :- {} ", msgToInventory);
        return ResponseEntity.ok(savedProd);
    }


    public void delete(Long id){
        productRepo.deleteById(id);

        InventoryMessage msgToInventory = InventoryMessage.builder()
                .productId(id)
                .quantity(0)
                .action("DELETE")
                .build();

        eventPublisher.sendToInventory(msgToInventory);
        log.info("Deleted product with id :- {} ", id);
    }

    public Product getProductById(Long id){
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
}
