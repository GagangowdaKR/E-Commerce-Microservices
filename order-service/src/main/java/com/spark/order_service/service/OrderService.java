package com.spark.order_service.service;

import com.spark.order_service.dto.OrderCheckResponse;
import com.spark.order_service.dto.PlaceOrderRequest;
import com.spark.order_service.dto.OrderResponse;
import com.spark.order_service.entity.Order;
import com.spark.order_service.entity.OrderItem;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(PlaceOrderRequest request);

    void updateOrderStatus(OrderCheckResponse orderCheckResponse);

    List<Order> getAllOrders();

    Order getOrder(String orderNumber);
}
