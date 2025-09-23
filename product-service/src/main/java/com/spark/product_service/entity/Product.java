package com.spark.product_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter @Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 2000)
    private String description;
    private Double price;

    public Product(String name, String description, Double price){
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public Product(){}
}
