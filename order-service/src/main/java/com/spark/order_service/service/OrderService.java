package com.spark.order_service.service;

import com.spark.order_service.dto.PlaceOrderRequest;
import com.spark.order_service.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request);

    void updateOrderStatus(String orderId, String status);
}
