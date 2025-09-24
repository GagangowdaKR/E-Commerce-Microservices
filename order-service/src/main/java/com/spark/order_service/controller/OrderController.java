package com.spark.order_service.controller;

import com.spark.order_service.dto.PlaceOrderRequest;
import com.spark.order_service.dto.OrderResponse;
import com.spark.order_service.entity.Order;
import com.spark.order_service.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    @PostMapping
    public OrderResponse placeOrder(@RequestBody PlaceOrderRequest request){
        return orderService.placeOrder(request);
    }

    @GetMapping
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderNumber}")
    public Order getOrder(@PathVariable String orderNumber){
        return orderService.getOrder(orderNumber);
    }
}
