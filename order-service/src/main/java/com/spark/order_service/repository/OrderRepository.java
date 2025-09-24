package com.spark.order_service.repository;

import com.spark.order_service.entity.Order;
import com.spark.order_service.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    public Order findByOrderNumber(String orderNumber);
}
