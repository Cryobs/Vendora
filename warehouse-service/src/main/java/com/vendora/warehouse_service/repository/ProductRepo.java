package com.vendora.warehouse_service.repository;

import com.vendora.warehouse_service.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepo  extends CrudRepository<ProductEntity, Long> {
    ProductEntity findByName(String name);
}
