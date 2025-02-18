package com.vendora.catalog_service.service;


import com.vendora.catalog_service.DTO.ProductDTO;
import com.vendora.catalog_service.entity.Product;
import com.vendora.catalog_service.repository.elasticsearch.ProductSearchRepo;
import com.vendora.catalog_service.repository.mongo.ProductsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductsRepo productsRepo;
    @Autowired
    private SyncService syncService;
    @Autowired
    private ProductSearchRepo productSearchRepo;

    public Product registerProduct(ProductDTO product, Jwt jwt){
        Product productEntity = new Product(jwt.getSubject(), product.getName(), product.getDescription(), product.getBasePrice(), product.getCategory(), product.getCharacteristics());
        Product finalProduct = productsRepo.save(productEntity);
        productSearchRepo.save(productEntity);
        return finalProduct;
    }

    public String deleteProduct(String productId, Jwt jwt) throws IllegalAccessException {
        Product productEntity = productsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (productEntity.getUserId().equals(jwt.getSubject())){
            productsRepo.delete(productEntity);
            productSearchRepo.delete(productEntity);
            return "Product deleted";
        } else {
            throw new IllegalAccessException("Unauthorized");
        }
    }

    public Product getProduct(String productId){
        return productsRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Page<Product> search(String keyword, int page, int psize) {
        Pageable pageable = PageRequest.of(page, psize);
        return productSearchRepo.findByNameContaining(keyword, pageable);
    }


    public Iterable<Product> productListAll(){
        return productsRepo.findAll();
    }
}