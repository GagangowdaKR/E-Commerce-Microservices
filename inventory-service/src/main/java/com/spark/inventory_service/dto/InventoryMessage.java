package com.spark.inventory_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InventoryMessage {

    private Long productId;
    private Integer quantity;
    private String action; // CREATE, UPDATE, DELETE
}
