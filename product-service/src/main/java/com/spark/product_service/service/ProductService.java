package com.spark.product_service.service;

import com.spark.product_service.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product create(Product product);

    ResponseEntity<Product> update(Long id, Product product);

    void delete(Long id);
}
