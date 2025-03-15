package com.vendora.catalog_service.scheduled;

import com.vendora.catalog_service.entity.ProductEntity;
import com.vendora.catalog_service.repository.elasticsearch.ProductSearchRepo;
import com.vendora.catalog_service.repository.postgres.ProductsRepo;
import jakarta.annotation.PostConstruct;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class ElasticSync {
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
                    System.out.println("Data inconsistency detected! Resyncing...");

                    // Получаем все продукты из БД
                    List<ProductEntity> allProducts = productsRepo.findAll();

                    // Получаем ID всех продуктов в Elasticsearch
                    Iterable<ProductEntity> esProductsIterable = productSearchRepo.findAll();
                    Set<UUID> esProductIds = StreamSupport.stream(esProductsIterable.spliterator(), false)
                            .map(ProductEntity::getId)
                            .collect(Collectors.toSet());

                    // Фильтруем продукты, которых нет в Elasticsearch
                    List<ProductEntity> missingProducts = allProducts.stream()
                            .filter(p -> !esProductIds.contains(p.getId()))
                            .toList();

                    if (!missingProducts.isEmpty()) {
                        productSearchRepo.saveAll(missingProducts);
                        System.out.println("Added " + missingProducts.size() + " missing products.");
                    } else {
                        System.out.println("No missing products found.");
                    }
                }
            } catch (Exception e) {
                System.err.println("Error during ElasticSearch sync: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    @PostConstruct
    public void init() {
        checkAndFixElasticSync();
    }
}

