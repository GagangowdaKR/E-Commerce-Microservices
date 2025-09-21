package com.spark.order_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class OrderResponse {

    private String orderNumber;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemDto> itemList;
}
