package com.vendora.catalog_service.scheduled;

import com.vendora.catalog_service.entity.ProductEntity;
import com.vendora.catalog_service.repository.elasticsearch.ProductSearchRepo;
import com.vendora.catalog_service.repository.postgres.ProductsRepo;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import java.util.List;
import java.util.Set;

@Component
public class ElasticSync {
    private static final String DATA_INCONSISTENCY_TEMPLATE = "Data inconsistency detected! Resyncing...";
    private static final String ADDED_PRODUCT_TEMPLATE = "Added {} missing products.";
    private static final String NO_MISSING_PRODUCTS_TEMPLATE = "No missing products found.";
    private static final String SYNC_ERROR_TEMPLATE = "Error during ElasticSearch sync: {}";

    private static final Logger log = LogManager.getLogger(ElasticSync.class);

    @Autowired
    private ProductsRepo productsRepo;

    @Autowired
    private ProductSearchRepo productSearchRepo;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Value("${scheduled.elastic-sync.fixed-rate}")
    private long fixedRate;

    @Scheduled(fixedRateString = "#{@elasticSync.getFixedRate()}")
    public void checkAndFixElasticSync() {
        taskScheduler.execute(() -> {
            try {
                long dbCount = productsRepo.count();
                long esCount = productSearchRepo.count();

                if (dbCount != esCount) {
                    log.info(DATA_INCONSISTENCY_TEMPLATE);

                    // We get all products from the database
                    List<ProductEntity> allProducts = productsRepo.findAll();

                    // Get IDs of all products in Elasticsearch
                    Iterable<ProductEntity> esProductsIterable = productSearchRepo.findAll();
                    Set<UUID> esProductIds = StreamSupport.stream(esProductsIterable.spliterator(), false)
                            .map(ProductEntity::getId)
                            .collect(Collectors.toSet());

                    // Filtering products that are not in Elasticsearch
                    List<ProductEntity> missingProducts = allProducts.stream()
                            .filter(p -> !esProductIds.contains(p.getId()))
                            .toList();

                    if (!missingProducts.isEmpty()) {
                        productSearchRepo.saveAll(missingProducts);
                        log.info(ADDED_PRODUCT_TEMPLATE, missingProducts.size());
                    } else {
                        log.info(NO_MISSING_PRODUCTS_TEMPLATE);
                    }
                }
            } catch (Exception e) {
                log.error(SYNC_ERROR_TEMPLATE,  e.getMessage());
            }
        });
    }

    @PostConstruct
    public void init() {
        checkAndFixElasticSync();
    }
}

