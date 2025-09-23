package com.spark.inventory_service.dto;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckResponse {

    private Long orderId;
    private String status;
}
