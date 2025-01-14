package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.InventoryMovementEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface InventoryMovementRepo extends CrudRepository<InventoryMovementEntity, UUID> {
}
