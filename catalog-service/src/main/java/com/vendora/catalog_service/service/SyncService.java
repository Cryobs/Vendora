package com.vendora.catalog_service.service;

import com.vendora.catalog_service.entity.Product;
import com.vendora.catalog_service.repository.elasticsearch.ProductSearchRepo;
import com.vendora.catalog_service.repository.mongo.ProductsRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SyncService {
    private static final Logger logger = LoggerFactory.getLogger(SyncService.class);
    private final ProductsRepo productRepo;
    private final ProductSearchRepo productSearchRepo;

    public SyncService(ProductsRepo productRepo, ProductSearchRepo productSearchRepo) {
        this.productRepo = productRepo;
        this.productSearchRepo = productSearchRepo;
    }

    @Transactional
    public void syncData() {
        logger.info("Starting data synchronization...");
        try {
            List<Product> products = productRepo.findAll();
            logger.info("Fetched {} products from MongoDB.", products.size());

            productSearchRepo.saveAll(products);
            logger.info("Successfully saved {} products to Elasticsearch.", products.size());
        } catch (Exception e) {
            logger.error("Error during synchronization", e);
            throw e; // Гарантируем откат транзакции при ошибке
        }
    }
}
