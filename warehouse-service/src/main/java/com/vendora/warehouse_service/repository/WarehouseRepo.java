package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.WarehouseEntity;
import org.springframework.data.repository.CrudRepository;

public interface WarehouseRepo extends CrudRepository<WarehouseEntity, Long> {

}
