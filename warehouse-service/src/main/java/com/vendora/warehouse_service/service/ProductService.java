package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public ProductEntity registerProduct(ProductEntity product){
        return productRepo.save(product);
    }
}
