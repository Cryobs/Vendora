package com.vendora.price_service.controller;

import com.vendora.price_service.DTO.TaxDTO;
import com.vendora.price_service.entity.TaxEntity;
import com.vendora.price_service.repository.TaxRepo;
import com.vendora.price_service.service.PriceService;
import com.vendora.price_service.service.TaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tax")
public class TaxController {
    @Autowired
    private TaxService taxService;
    @Autowired
    private TaxRepo taxRepo;

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TaxEntity> setTax(@RequestBody TaxDTO tax){
        return ResponseEntity.ok(taxService.setTax(tax));
    }

    @GetMapping("/list")
    public ResponseEntity<Page<TaxEntity>> getTaxList(Pageable pageable){
        return ResponseEntity.ok(taxService.getTaxList(pageable));
    }

}
