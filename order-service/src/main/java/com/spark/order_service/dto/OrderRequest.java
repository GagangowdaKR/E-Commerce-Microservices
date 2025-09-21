package com.spark.order_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    private String userId;
    private List<OrderItemDto> itemList;
}
