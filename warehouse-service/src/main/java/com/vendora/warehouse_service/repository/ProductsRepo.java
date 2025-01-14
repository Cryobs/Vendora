package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ProductsRepo extends CrudRepository<ProductEntity, UUID> {
    ProductEntity findByName(String name);
}
