package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.TaxDTO;
import com.vendora.price_service.entity.TaxEntity;
import com.vendora.price_service.repository.TaxRepo;
import com.vendora.price_service.service.PriceService;
import com.vendora.price_service.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tax")
public class TaxController {
    @Autowired
    private TaxService taxService;
    @Autowired
    private TaxRepo taxRepo;

    @PostMapping
    public ResponseEntity<TaxEntity> setTax(@RequestBody TaxDTO tax){
        return ResponseEntity.ok(taxService.setTax(tax));
    }

    @GetMapping("/list")
    public ResponseEntity<Iterable<TaxEntity>> getTaxList(){
        return ResponseEntity.ok(taxRepo.findAll());
    }

}
