package com.spark.inventory_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "inventory")
public class Inventory {

    @Id
    private Long productId;

    private Integer stock;

}
