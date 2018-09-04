package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.model.PricingPeriod;
import com.numlabs.portfoliomanager.model.PricingPeriodType;
import com.numlabs.portfoliomanager.service.PricingPeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PricingPeriodController {

    @Autowired
    private PricingPeriodService pricingPeriodService;

    @RequestMapping("period/pricing/types/all")
    public ResponseEntity<List<PricingPeriodType>> getAllPricingPeriods() {
        List<PricingPeriodType> types = pricingPeriodService.getAllPricingPeriodTypes();

        return new ResponseEntity<>(types, HttpStatus.OK);
    }
}
