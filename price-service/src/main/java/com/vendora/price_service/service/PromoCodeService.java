package com.vendora.price_service.service;


import com.vendora.price_service.DTO.PromoCreateDTO;
import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.repository.PromoCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepo promoCodeRepo;

    public ArrayList<PromoCodeEntity> getActivePromo(){
        return promoCodeRepo.findByIsActive(true);
    }

    public PromoCodeEntity updatePromo(String promo){
        PromoCodeEntity newPromo = promoCodeRepo.findByCode(promo).get(0);
        newPromo.setActive(!newPromo.isActive());
        return promoCodeRepo.save(newPromo);
    }

    public PromoCodeEntity createPromo(PromoCreateDTO promo){
        PromoCodeEntity promoCodeEntity = new PromoCodeEntity(
                promo.getCode(),
                promo.getDiscountType(),
                promo.getDiscountValue(),
                promo.getMaxUses(),
                promo.getCurrentUses(),
                promo.getStartDate(),
                promo.getEndDate(),
                promo.isActive());
        return promoCodeRepo.save(promoCodeEntity);
    }

    public BigDecimal calculatePromoCode(String promoCode, BigDecimal price){
        BigDecimal code;
        if (isPromoActive(promoCode)) {
            PromoCodeEntity promoCodeEntity = promoCodeRepo.findByCodeAndIsActive(promoCode, true).get();
            if (Objects.equals(promoCodeEntity.getDiscountType(), "Percent")) {
                code = price.multiply(promoCodeEntity.getDiscountValue()).divide(BigDecimal.valueOf(100), 2);
            } else {
                code = promoCodeEntity.getDiscountValue();
            }
        } else {
            code = BigDecimal.ZERO;
        }
        return code;
    }

    public boolean isPromoActive(String code){
        return promoCodeRepo.findByCodeAndIsActive(code, true).isPresent();
    }
}
