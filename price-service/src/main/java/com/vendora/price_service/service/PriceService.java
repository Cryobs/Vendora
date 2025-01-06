package com.vendora.price_service.service;


import com.vendora.price_service.DTO.PriceCalculateDTO;
import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.entity.TaxEntity;
import com.vendora.price_service.repository.PromoCodeRepo;
import com.vendora.price_service.repository.ShippingRepo;
import com.vendora.price_service.repository.TaxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class PriceService {


    @Autowired
    private TaxRepo taxRepo;
    @Autowired
    private ShippingRepo shippingRepo;
    @Autowired
    private PromoCodeRepo promoCodeRepo;

    public BigDecimal calculatePrice(PriceCalculateDTO request){
        String promo = request.getPromoCode();
        BigDecimal grossPrice = request.getGrossPrice();
        String shippingType = request.getShippingType();

        // 1. Налог
        BigDecimal taxRate = taxRepo.findById(1L)
                .map(TaxEntity::getTax)
                .orElse(BigDecimal.ZERO);
        BigDecimal tax = grossPrice.multiply(taxRate).divide(BigDecimal.valueOf(100), 2);

        // 2. Скидка
        BigDecimal discount = promoCodeRepo.findByCodeAndIsActive(promo, true)
                .map(PromoCodeEntity::getDiscount)
                .orElse(BigDecimal.ZERO);
        BigDecimal discountAmount = grossPrice.multiply(discount).divide(BigDecimal.valueOf(100));

        // 3. Доставка
        BigDecimal shippingCost = shippingRepo.findById(shippingType)
                .map(ShippingEntity::getPrice)
                .orElse(BigDecimal.ZERO);

        // 4. Финальная цена
        return grossPrice.add(tax).subtract(discountAmount).add(shippingCost);

    }
}
