package com.vendora.catalog_service.repository.postgres;


import com.vendora.catalog_service.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductsRepo extends JpaRepository<Product, UUID> {
}