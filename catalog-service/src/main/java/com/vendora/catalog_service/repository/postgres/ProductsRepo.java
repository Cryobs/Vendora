package com.vendora.catalog_service.repository.postgres;


import com.vendora.catalog_service.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductsRepo extends JpaRepository<ProductEntity, UUID> {
}