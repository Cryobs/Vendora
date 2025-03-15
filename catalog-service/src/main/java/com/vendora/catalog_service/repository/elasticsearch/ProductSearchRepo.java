package com.vendora.catalog_service.repository.elasticsearch;

import com.vendora.catalog_service.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductSearchRepo extends ElasticsearchRepository<ProductEntity, UUID> {
    Page<ProductEntity> findByNameContaining(String query, Pageable pageable);
}
