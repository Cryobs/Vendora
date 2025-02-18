package com.vendora.catalog_service.repository.mongo;


import com.vendora.catalog_service.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductsRepo extends MongoRepository<Product, String> {
}