package com.spark.order_service.dto;

import lombok.Getter;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Getter @Setter
public class OrderCreatedEvent {

    private String orderNumber;
    private String userId;
    private List<OrderItemDto> itemList;

    public OrderCreatedEvent(String orderNumber, String userId, List<OrderItemDto> itemList) {
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.itemList = itemList;
    }
}
