package com.spark.inventory_service.repository;

import com.spark.inventory_service.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
