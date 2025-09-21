package com.spark.order_service.service;

import com.spark.order_service.dto.OrderRequest;
import com.spark.order_service.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);
}
