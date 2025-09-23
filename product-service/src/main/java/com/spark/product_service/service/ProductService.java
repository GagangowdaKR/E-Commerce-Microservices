package com.spark.product_service.service;

import com.spark.product_service.dto.ProductDto;
import com.spark.product_service.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product getProductById(Long id);

    Product create(ProductDto productDto);

    ResponseEntity<Product> update(Long id, ProductDto productDto);

    void delete(Long id);
}
