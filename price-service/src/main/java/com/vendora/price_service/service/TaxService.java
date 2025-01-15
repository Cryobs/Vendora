package com.vendora.price_service.service;

import com.vendora.price_service.DTO.TaxDTO;
import com.vendora.price_service.entity.TaxEntity;
import com.vendora.price_service.repository.TaxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TaxService {
    @Autowired
    private TaxRepo taxRepo;

    public TaxEntity setTax(TaxDTO tax){
        if(taxRepo.findByRegion(tax.getRegion()).isPresent()){
            TaxEntity taxEntity = taxRepo.findByRegion(tax.getRegion()).get();
            return taxRepo.save(taxEntity);
        } else {
            TaxEntity taxEntity = new TaxEntity(tax.getRegion(), tax.getTax_type(), tax.getRate());
            return taxRepo.save(taxEntity);
        }
    }

    public BigDecimal calculateTax(BigDecimal price, String region){
        return price
                .divide(BigDecimal.valueOf(100), 2)
                .multiply(taxRepo.findByRegion(region).orElseThrow().getRate());
    }
}
