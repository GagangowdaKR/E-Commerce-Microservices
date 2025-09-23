package com.spark.product_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Data @Builder
public class InventoryMessage {

    private Long productId;
    private Integer quantity;
    private String action; // CREATE, UPDATE, DELETE
}
