package com.vendora.price_service.service;


import com.vendora.price_service.entity.PromoCodeEntity;
import com.vendora.price_service.repository.PromoCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepo promoCodeRepo;

    public ArrayList<PromoCodeEntity> getActivePromo(){
        return promoCodeRepo.findByIsActive(true);
    }

    public PromoCodeEntity updatePromo(String promo){
        PromoCodeEntity newPromo = promoCodeRepo.findById(promo).get();
        newPromo.setActive(!newPromo.isActive());
        return promoCodeRepo.save(newPromo);
    }

    public PromoCodeEntity createPromo(PromoCodeEntity promo){
        return promoCodeRepo.save(promo);
    }
}
