package com.spark.order_service.dto;

import lombok.Data;

@Data
public class OrderItemDto {

    private Long productId;
    private Integer quantity;
}