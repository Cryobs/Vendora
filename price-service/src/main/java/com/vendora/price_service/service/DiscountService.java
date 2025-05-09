package com.vendora.price_service.service;

import com.vendora.price_service.DTO.DiscountDTO;
import com.vendora.price_service.entity.DiscountEntity;
import com.vendora.price_service.exception.NoDiscountException;
import com.vendora.price_service.feign.CatalogClient;
import com.vendora.price_service.repository.DiscountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.UUID;

@Service
public class DiscountService {

    private static final String NO_DISCOUNT_TEMPLATE = "Product with id %s don't have discount";
    @Autowired
    private DiscountRepo discountRepo;
    @Autowired
    private CatalogClient catalogClient;

    public DiscountEntity setDiscount(DiscountDTO discountDTO){
        if(discountRepo.findByProductId(discountDTO.getProductId()).isPresent()){
            DiscountEntity discount = discountRepo.findByProductId(discountDTO.getProductId()).get();
            return discountRepo.save(discount);
        } else {
            DiscountEntity discount = new DiscountEntity(discountDTO, catalogClient.getProduct(discountDTO.getProductId()).getBody());
            return discountRepo.save(discount);
        }
    }

    public DiscountEntity updateDiscount(UUID productId){
        DiscountEntity discount = discountRepo.findByProductId(productId).get();
        discount.setActive(!discount.isActive());
        return discountRepo.save(discount);
    }

    public DiscountEntity haveDiscount(UUID product_id){
        return discountRepo.findByProductId(product_id)
                .orElseThrow(() ->new NoDiscountException(NO_DISCOUNT_TEMPLATE.formatted(product_id)));
    }

    public BigDecimal calculateDiscount(UUID productId, BigDecimal price){
        DiscountEntity discount;
        BigDecimal discount_value;
        try {
            discount = haveDiscount(productId);
            if (Objects.equals(discount.getDiscountType(), "Percent")) {
                discount_value = price
                        .divide(BigDecimal.valueOf(100), RoundingMode.valueOf(2))
                        .multiply(discount.getDiscountValue());
            } else {
                discount_value = discount.getDiscountValue();
            }
        } catch (Exception e) {
            discount_value = BigDecimal.ZERO;
        }
        return discount_value;
    }

    public Page<DiscountEntity> getDiscountList(Pageable pageable){
        return discountRepo.findAll(pageable);
    }
}
