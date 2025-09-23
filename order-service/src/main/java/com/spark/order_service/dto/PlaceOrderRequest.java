package com.spark.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {

    private String userId;
    private List<OrderItemDto> itemList; // (prodId, quantity)
}
