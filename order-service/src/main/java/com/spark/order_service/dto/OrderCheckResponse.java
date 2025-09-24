package com.spark.order_service.dto;

import lombok.*;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckResponse {
    private Long orderId;
    private String status;
    private String userId;
}