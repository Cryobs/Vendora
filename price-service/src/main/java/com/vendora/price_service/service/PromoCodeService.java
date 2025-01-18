package com.vendora.price_service.service;


import com.vendora.price_service.DTO.PromoCreateDTO;
import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.exception.PromoCodeExpiredException;
import com.vendora.price_service.exception.PromoCodeNotStartedException;
import com.vendora.price_service.exception.UndefinedPromoCodeException;
import com.vendora.price_service.repository.PromoCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepo promoCodeRepo;

    public ArrayList<PromoCodeEntity> getActivePromo() {
        List<PromoCodeEntity> activePromoCodes = promoCodeRepo.findByIsActive(true)
                .orElseThrow(() -> new UndefinedPromoCodeException("Undefined Promo Code"));

        return new ArrayList<>(activePromoCodes);
    }


    public PromoCodeEntity updatePromo(String promo){
        PromoCodeEntity newPromo = promoCodeRepo.findByCode(promo)
                .orElseThrow(() -> new UndefinedPromoCodeException("Undefined Promo Code")).getFirst();
        newPromo.setActive(!newPromo.isActive());
        return promoCodeRepo.save(newPromo);
    }

    public boolean usePromo(String promo){
        PromoCodeEntity newPromo = promoCodeRepo.findByCode(promo)
                .orElseThrow(() -> new UndefinedPromoCodeException("Undefined Promo Code")).getFirst();
        if (newPromo.getMaxUses() > newPromo.getCurrentUses()){
            newPromo.setCurrentUses(newPromo.getCurrentUses() + 1);
        }
        if (newPromo.getMaxUses() == newPromo.getCurrentUses() || LocalDateTime.now().isAfter(newPromo.getEndDate())){
            newPromo.setActive(false);
            promoCodeRepo.save(newPromo);
            throw new PromoCodeExpiredException("Promo code " + promo +" expired");
        } else if (LocalDateTime.now().isBefore(newPromo.getStartDate())){
            throw new PromoCodeNotStartedException("Promo code hasn't started yet, will started at " + newPromo.getStartDate());
        }
        promoCodeRepo.save(newPromo);
        return true;
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
        BigDecimal code = BigDecimal.ZERO;
        if (isPromoActive(promoCode)) {
            PromoCodeEntity promoCodeEntity = promoCodeRepo.findByCodeAndIsActive(promoCode, true).get();
            if (Objects.equals(promoCodeEntity.getDiscountType(), "Percent")) {
                code = price.multiply(promoCodeEntity.getDiscountValue()).divide(BigDecimal.valueOf(100), 2);
            } else if (Objects.equals(promoCodeEntity.getDiscountType(), "Fix")) {
                code = promoCodeEntity.getDiscountValue();
            }
        }
        return code;
    }

    public boolean isPromoActive(String code){
        return promoCodeRepo.findByCodeAndIsActive(code, true).isPresent();
    }
}
