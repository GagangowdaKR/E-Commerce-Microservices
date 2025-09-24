package com.spark.notification_service.dto;

import lombok.*;

@Data
@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderNotification {
    private Long orderId;
    private String status;
    private String userId;
}