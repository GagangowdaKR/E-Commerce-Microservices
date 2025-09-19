package com.spark.product_service.service.impl;

import com.spark.product_service.entity.Product;
import com.spark.product_service.event.ProductEventPublisher;
import com.spark.product_service.repository.ProductRepository;
import com.spark.product_service.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final ProductEventPublisher eventPublisher;

    public ProductServiceImpl(ProductRepository productRepo, ProductEventPublisher eventPublisher) {
        this.productRepo = productRepo;
        this.eventPublisher = eventPublisher;
    }

    public Product create(Product product){
        Product savedProd = productRepo.save(product);
        eventPublisher.publishCreated(savedProd);
        return savedProd;
    }

    public ResponseEntity<Product> update(Long id, Product product){
        Product dbProduct = productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
        dbProduct.setName(product.getName());
        dbProduct.setDescription(product.getDescription());
        dbProduct.setPrice(product.getPrice());
        dbProduct.setStock(product.getStock());

        Product savedProd = productRepo.save(dbProduct);
        eventPublisher.publishUpdates(savedProd);
        return ResponseEntity.ok(savedProd);
    }

    public void delete(Long id){
        productRepo.deleteById(id);
        eventPublisher.publishDeleted(id);
    }

    public Product getProductById(Long id){
        return productRepo.findById(id).orElseThrow(() -> new RuntimeException("Product Not Found"));
    }

    public List<Product> getAllProducts(){
        return productRepo.findAll();
    }
}
