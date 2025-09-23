package com.spark.order_service.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String orderNumber;

    private String userId;

    private LocalDateTime orderDate;

    private String status; // Pending, Confirmed, Canceled

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> itemList;

}
