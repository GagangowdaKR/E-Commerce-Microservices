package com.spark.product_service.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ProductDto {

    private String name;
    @Column(length = 2000)
    private String description;
    private Double price;
    private Integer stock;
}
