package com.vendora.warehouse_service.service;

import com.vendora.warehouse_service.DTO.ProductDTO;
import com.vendora.warehouse_service.entity.ProductEntity;
import com.vendora.warehouse_service.repository.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductsRepo productsRepo;

    public ProductEntity registerProduct(ProductDTO product){
        ProductEntity productEntity = new ProductEntity(product.getName(), product.getDescription(), product.getBasePrice());
        return productsRepo.save(productEntity);
    }

    public Iterable<ProductEntity> productListAll(){
        return productsRepo.findAll();
    }
}
