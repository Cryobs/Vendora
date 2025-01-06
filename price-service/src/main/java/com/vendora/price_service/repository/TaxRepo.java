package com.vendora.price_service.repository;

import com.vendora.price_service.entity.TaxEntity;
import org.springframework.data.repository.CrudRepository;

public interface TaxRepo extends CrudRepository<TaxEntity, Long> {
}
