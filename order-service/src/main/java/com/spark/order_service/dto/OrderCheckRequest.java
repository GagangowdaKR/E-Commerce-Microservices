package com.spark.order_service.dto;

import lombok.*;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class OrderCheckRequest {

    private String userId;
    private Long orderId;
    private Map<Long, Integer> productQuantities; //
}
