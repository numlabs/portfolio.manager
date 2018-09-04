package com.numlabs.portfoliomanager.controller;

import com.numlabs.portfoliomanager.service.PeriodService;
import com.numlabs.portfoliomanager.util.CompanyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminController {

    @Autowired
    private PeriodService periodService;

    @Autowired
    private CompanyUtil companyUtil;



    @RequestMapping("admin/period/margins/calculate")
    public ResponseEntity<String> calculatePeriodMargins() {
        periodService.calculatePeriodMargins();
        return new ResponseEntity<>("Operation completed successfully.", HttpStatus.OK);
    }

    @RequestMapping("companies/update/prices")
    public ResponseEntity<String> updateCompanyPrices() {
        companyUtil.updateCompanyPricesOfBIST();
        return new ResponseEntity<>("Successful.", HttpStatus.OK);
    }

}
