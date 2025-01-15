package com.vendora.price_service.service;

import com.vendora.price_service.DTO.ShippingDTO;
import com.vendora.price_service.entity.ShippingEntity;
import com.vendora.price_service.repository.ShippingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingService {

    @Autowired
    private ShippingRepo shippingRepo;

    public ShippingEntity createShipping(ShippingDTO shipping){
        return shippingRepo.save(new ShippingEntity(shipping.getZone(), shipping.getWeightLimit(), shipping.getPrice(), shipping.getDeliveryType()));
    }

    public Iterable<ShippingEntity> getShippingList(){
        return shippingRepo.findAll();
    }

}
